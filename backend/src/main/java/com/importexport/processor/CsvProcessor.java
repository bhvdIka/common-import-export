package com.importexport.processor;

import com.opencsv.CSVReader;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;

@Component
public class CsvProcessor implements FileProcessor {

    private static final String[] SUPPORTED_EXTENSIONS = {"csv"};

    @Override
    public List<Map<String, Object>> processFile(InputStream inputStream) throws Exception {
        List<Map<String, Object>> result = new ArrayList<>();
        
        try (CSVReader reader = new CSVReader(new InputStreamReader(inputStream))) {
            String[] headers = reader.readNext();
            if (headers == null) {
                throw new IllegalArgumentException("CSV file is empty or has no headers");
            }

            String[] row;
            while ((row = reader.readNext()) != null) {
                Map<String, Object> rowData = new LinkedHashMap<>();
                for (int i = 0; i < headers.length && i < row.length; i++) {
                    rowData.put(headers[i].trim(), row[i].trim());
                }
                result.add(rowData);
            }
        }
        
        return result;
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