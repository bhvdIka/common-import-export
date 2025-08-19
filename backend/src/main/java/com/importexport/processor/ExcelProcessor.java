package com.importexport.processor;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.util.*;

@Component
public class ExcelProcessor implements FileProcessor {

    private static final String[] SUPPORTED_EXTENSIONS = {"xlsx", "xls"};

    @Override
    public List<Map<String, Object>> processFile(InputStream inputStream) throws Exception {
        List<Map<String, Object>> result = new ArrayList<>();
        
        try (Workbook workbook = createWorkbook(inputStream)) {
            Sheet sheet = workbook.getSheetAt(0); // Process first sheet
            
            if (sheet.getPhysicalNumberOfRows() == 0) {
                throw new IllegalArgumentException("Excel file is empty");
            }

            // Get headers from first row
            Row headerRow = sheet.getRow(0);
            if (headerRow == null) {
                throw new IllegalArgumentException("Excel file has no header row");
            }

            List<String> headers = new ArrayList<>();
            for (Cell cell : headerRow) {
                headers.add(getCellValueAsString(cell).trim());
            }

            // Process data rows
            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row == null) continue;

                Map<String, Object> rowData = new LinkedHashMap<>();
                for (int j = 0; j < headers.size(); j++) {
                    Cell cell = row.getCell(j);
                    String value = cell != null ? getCellValueAsString(cell).trim() : "";
                    rowData.put(headers.get(j), value);
                }
                result.add(rowData);
            }
        }
        
        return result;
    }

    private Workbook createWorkbook(InputStream inputStream) throws Exception {
        try {
            return new XSSFWorkbook(inputStream);
        } catch (Exception e) {
            inputStream.reset();
            return new HSSFWorkbook(inputStream);
        }
    }

    private String getCellValueAsString(Cell cell) {
        if (cell == null) return "";
        
        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue();
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    return cell.getDateCellValue().toString();
                } else {
                    return String.valueOf(cell.getNumericCellValue());
                }
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            case FORMULA:
                return cell.getCellFormula();
            default:
                return "";
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