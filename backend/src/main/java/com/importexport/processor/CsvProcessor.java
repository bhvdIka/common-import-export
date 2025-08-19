package com.importexport.processor;

import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.RFC4180Parser;
import com.opencsv.RFC4180ParserBuilder;
import com.opencsv.exceptions.CsvException;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.charset.Charset;
import java.util.*;

@Component
public class CsvProcessor implements FileProcessor {

    private static final String[] SUPPORTED_EXTENSIONS = {"csv"};
    
    // Supported delimiters
    private static final char[] DELIMITERS = {',', ';', '\t', '|'};

    @Override
    public List<Map<String, Object>> processFile(InputStream inputStream) throws Exception {
        List<Map<String, Object>> result = new ArrayList<>();
        
        // Try to detect encoding
        InputStreamReader reader = createReaderWithEncoding(inputStream);
        
        try {
            // Try different delimiters to find the best match
            char bestDelimiter = detectDelimiter(reader);
            
            // Reset reader
            reader = createReaderWithEncoding(inputStream);
            
            // Create parser with detected delimiter
            RFC4180Parser parser = new RFC4180ParserBuilder()
                    .withSeparator(bestDelimiter)
                    .withQuoteChar('"')
                    .build();
            
            try (CSVReader csvReader = new CSVReaderBuilder(reader)
                    .withCSVParser(parser)
                    .withSkipLines(0)
                    .build()) {
                
                List<String[]> allLines = csvReader.readAll();
                
                if (allLines.isEmpty()) {
                    throw new IllegalArgumentException("CSV file is empty");
                }
                
                String[] headers = allLines.get(0);
                validateHeaders(headers);
                
                // Process data rows
                for (int i = 1; i < allLines.size(); i++) {
                    String[] row = allLines.get(i);
                    
                    // Skip empty rows
                    if (isEmptyRow(row)) {
                        continue;
                    }
                    
                    Map<String, Object> rowData = new LinkedHashMap<>();
                    for (int j = 0; j < headers.length; j++) {
                        String header = headers[j].trim();
                        String value = j < row.length ? row[j].trim() : "";
                        
                        // Handle null and empty values
                        if ("null".equalsIgnoreCase(value) || "".equals(value)) {
                            rowData.put(header, null);
                        } else {
                            rowData.put(header, convertValue(value));
                        }
                    }
                    result.add(rowData);
                }
            }
            
        } catch (CsvException e) {
            throw new Exception("Error parsing CSV file: " + e.getMessage(), e);
        } catch (Exception e) {
            throw new Exception("Error processing CSV file: " + e.getMessage(), e);
        }
        
        return result;
    }

    private InputStreamReader createReaderWithEncoding(InputStream inputStream) {
        // Try UTF-8 first, fall back to system default
        try {
            return new InputStreamReader(inputStream, StandardCharsets.UTF_8);
        } catch (Exception e) {
            return new InputStreamReader(inputStream, Charset.defaultCharset());
        }
    }
    
    private char detectDelimiter(InputStreamReader reader) throws Exception {
        // Read first few lines to detect delimiter
        char[] buffer = new char[1024];
        reader.read(buffer);
        String sample = new String(buffer);
        
        int maxCount = 0;
        char bestDelimiter = ',';
        
        for (char delimiter : DELIMITERS) {
            int count = countOccurrences(sample, delimiter);
            if (count > maxCount) {
                maxCount = count;
                bestDelimiter = delimiter;
            }
        }
        
        return bestDelimiter;
    }
    
    private int countOccurrences(String text, char character) {
        int count = 0;
        for (int i = 0; i < text.length(); i++) {
            if (text.charAt(i) == character) {
                count++;
            }
        }
        return count;
    }
    
    private void validateHeaders(String[] headers) throws Exception {
        if (headers == null || headers.length == 0) {
            throw new IllegalArgumentException("CSV file has no headers");
        }
        
        Set<String> headerSet = new HashSet<>();
        for (String header : headers) {
            String trimmedHeader = header.trim();
            if (trimmedHeader.isEmpty()) {
                throw new IllegalArgumentException("CSV file contains empty header");
            }
            if (headerSet.contains(trimmedHeader)) {
                throw new IllegalArgumentException("CSV file contains duplicate header: " + trimmedHeader);
            }
            headerSet.add(trimmedHeader);
        }
    }
    
    private boolean isEmptyRow(String[] row) {
        if (row == null || row.length == 0) {
            return true;
        }
        
        for (String cell : row) {
            if (cell != null && !cell.trim().isEmpty()) {
                return false;
            }
        }
        return true;
    }
    
    private Object convertValue(String value) {
        if (value == null || value.trim().isEmpty()) {
            return null;
        }
        
        String trimmed = value.trim();
        
        // Try to detect numeric values
        if (trimmed.matches("-?\\d+")) {
            try {
                return Integer.parseInt(trimmed);
            } catch (NumberFormatException e) {
                // Fall back to string
            }
        }
        
        if (trimmed.matches("-?\\d*\\.\\d+")) {
            try {
                return Double.parseDouble(trimmed);
            } catch (NumberFormatException e) {
                // Fall back to string
            }
        }
        
        // Try to detect boolean values
        if ("true".equalsIgnoreCase(trimmed) || "yes".equalsIgnoreCase(trimmed) || "1".equals(trimmed)) {
            return true;
        }
        if ("false".equalsIgnoreCase(trimmed) || "no".equalsIgnoreCase(trimmed) || "0".equals(trimmed)) {
            return false;
        }
        
        return trimmed;
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