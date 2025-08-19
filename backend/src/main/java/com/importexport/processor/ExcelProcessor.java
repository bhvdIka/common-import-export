package com.importexport.processor;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

@Component
public class ExcelProcessor implements FileProcessor {

    private static final String[] SUPPORTED_EXTENSIONS = {"xlsx", "xls"};

    @Override
    public List<Map<String, Object>> processFile(InputStream inputStream) throws Exception {
        List<Map<String, Object>> result = new ArrayList<>();
        
        try (Workbook workbook = createWorkbook(inputStream)) {
            FormulaEvaluator evaluator = workbook.getCreationHelper().createFormulaEvaluator();
            
            // Process first sheet by default
            Sheet sheet = workbook.getSheetAt(0);
            
            if (sheet.getPhysicalNumberOfRows() == 0) {
                throw new IllegalArgumentException("Excel file is empty");
            }

            // Get headers from first row
            Row headerRow = sheet.getRow(sheet.getFirstRowNum());
            if (headerRow == null) {
                throw new IllegalArgumentException("Excel file has no header row");
            }

            List<String> headers = extractHeaders(headerRow);
            validateHeaders(headers);

            // Process data rows
            for (int i = sheet.getFirstRowNum() + 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row == null || isEmptyRow(row)) {
                    continue; // Skip empty rows
                }

                Map<String, Object> rowData = new LinkedHashMap<>();
                for (int j = 0; j < headers.size(); j++) {
                    String header = headers.get(j);
                    Cell cell = row.getCell(j);
                    Object value = getCellValue(cell, evaluator);
                    rowData.put(header, value);
                }
                result.add(rowData);
            }
            
            // Handle merged cells if any
            handleMergedCells(sheet, result, headers);
        }
        
        return result;
    }

    private Workbook createWorkbook(InputStream inputStream) throws Exception {
        try {
            // Try .xlsx format first
            return new XSSFWorkbook(inputStream);
        } catch (Exception e) {
            try {
                // Try .xls format
                return new HSSFWorkbook(inputStream);
            } catch (Exception e2) {
                throw new Exception("Unable to read Excel file. Supported formats: .xlsx, .xls", e2);
            }
        }
    }

    private List<String> extractHeaders(Row headerRow) {
        List<String> headers = new ArrayList<>();
        for (Cell cell : headerRow) {
            String header = getCellValueAsString(cell);
            if (header != null && !header.trim().isEmpty()) {
                headers.add(header.trim());
            } else {
                headers.add("Column_" + (cell.getColumnIndex() + 1)); // Generate header for empty cells
            }
        }
        return headers;
    }
    
    private void validateHeaders(List<String> headers) throws Exception {
        if (headers.isEmpty()) {
            throw new IllegalArgumentException("Excel file has no valid headers");
        }
        
        Set<String> headerSet = new HashSet<>();
        for (String header : headers) {
            if (headerSet.contains(header)) {
                throw new IllegalArgumentException("Excel file contains duplicate header: " + header);
            }
            headerSet.add(header);
        }
    }
    
    private boolean isEmptyRow(Row row) {
        if (row == null) {
            return true;
        }
        
        for (Cell cell : row) {
            if (cell != null && cell.getCellType() != CellType.BLANK) {
                String value = getCellValueAsString(cell);
                if (value != null && !value.trim().isEmpty()) {
                    return false;
                }
            }
        }
        return true;
    }

    private Object getCellValue(Cell cell, FormulaEvaluator evaluator) {
        if (cell == null) {
            return null;
        }

        try {
            switch (cell.getCellType()) {
                case STRING:
                    String stringValue = cell.getStringCellValue();
                    return stringValue != null ? stringValue.trim() : null;
                    
                case NUMERIC:
                    if (DateUtil.isCellDateFormatted(cell)) {
                        Date date = cell.getDateCellValue();
                        return date != null ? LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault()) : null;
                    } else {
                        double numericValue = cell.getNumericCellValue();
                        // Check if it's actually an integer
                        if (numericValue == Math.floor(numericValue) && !Double.isInfinite(numericValue)) {
                            return (long) numericValue;
                        } else {
                            return numericValue;
                        }
                    }
                    
                case BOOLEAN:
                    return cell.getBooleanCellValue();
                    
                case FORMULA:
                    try {
                        CellValue cellValue = evaluator.evaluate(cell);
                        return evaluateFormulaCell(cellValue);
                    } catch (Exception e) {
                        // Return formula as string if evaluation fails
                        return cell.getCellFormula();
                    }
                    
                case BLANK:
                    return null;
                    
                case ERROR:
                    return "#ERROR";
                    
                default:
                    return getCellValueAsString(cell);
            }
        } catch (Exception e) {
            // Fall back to string representation
            return getCellValueAsString(cell);
        }
    }
    
    private Object evaluateFormulaCell(CellValue cellValue) {
        if (cellValue == null) {
            return null;
        }
        
        switch (cellValue.getCellType()) {
            case STRING:
                return cellValue.getStringValue();
            case NUMERIC:
                double numericValue = cellValue.getNumberValue();
                if (numericValue == Math.floor(numericValue) && !Double.isInfinite(numericValue)) {
                    return (long) numericValue;
                } else {
                    return numericValue;
                }
            case BOOLEAN:
                return cellValue.getBooleanValue();
            case ERROR:
                return "#ERROR";
            default:
                return null;
        }
    }

    private String getCellValueAsString(Cell cell) {
        if (cell == null) {
            return "";
        }

        try {
            switch (cell.getCellType()) {
                case STRING:
                    return cell.getStringCellValue();
                case NUMERIC:
                    if (DateUtil.isCellDateFormatted(cell)) {
                        return cell.getDateCellValue().toString();
                    } else {
                        double numericValue = cell.getNumericCellValue();
                        if (numericValue == Math.floor(numericValue) && !Double.isInfinite(numericValue)) {
                            return String.valueOf((long) numericValue);
                        } else {
                            return String.valueOf(numericValue);
                        }
                    }
                case BOOLEAN:
                    return String.valueOf(cell.getBooleanCellValue());
                case FORMULA:
                    return cell.getCellFormula();
                case BLANK:
                    return "";
                case ERROR:
                    return "#ERROR";
                default:
                    return "";
            }
        } catch (Exception e) {
            return "";
        }
    }
    
    private void handleMergedCells(Sheet sheet, List<Map<String, Object>> result, List<String> headers) {
        // Handle merged cells by propagating values
        for (CellRangeAddress mergedRegion : sheet.getMergedRegions()) {
            int firstRow = mergedRegion.getFirstRow();
            int lastRow = mergedRegion.getLastRow();
            int firstCol = mergedRegion.getFirstColumn();
            int lastCol = mergedRegion.getLastColumn();
            
            // Skip if this is in the header row
            if (firstRow == 0) {
                continue;
            }
            
            // Get the value from the first cell
            Row row = sheet.getRow(firstRow);
            if (row != null) {
                Cell cell = row.getCell(firstCol);
                Object value = getCellValue(cell, null);
                
                // Propagate to all cells in the merged region
                for (int r = firstRow; r <= lastRow; r++) {
                    for (int c = firstCol; c <= lastCol; c++) {
                        if (r - 1 < result.size() && c < headers.size()) {
                            Map<String, Object> rowData = result.get(r - 1); // -1 because we skip header
                            String header = headers.get(c);
                            if (!rowData.containsKey(header) || rowData.get(header) == null) {
                                rowData.put(header, value);
                            }
                        }
                    }
                }
            }
        }
    }

    @Override
    public String[] getSupportedExtensions() {
        return SUPPORTED_EXTENSIONS.clone();
    }

    @Override
    public boolean supports(String fileType) {
        return Arrays.asList(SUPPORTED_EXTENSIONS).contains(fileType.toLowerCase());
    }
}