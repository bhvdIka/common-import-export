package com.importexport.processor;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.util.*;

@Component
public class JsonProcessor implements FileProcessor {

    private static final String[] SUPPORTED_EXTENSIONS = {"json"};
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public List<Map<String, Object>> processFile(InputStream inputStream) throws Exception {
        List<Map<String, Object>> result = new ArrayList<>();
        
        JsonNode rootNode = objectMapper.readTree(inputStream);
        
        if (rootNode.isArray()) {
            // Handle array of objects
            for (JsonNode node : rootNode) {
                if (node.isObject()) {
                    Map<String, Object> rowData = objectMapper.convertValue(node, Map.class);
                    result.add(rowData);
                }
            }
        } else if (rootNode.isObject()) {
            // Handle single object
            Map<String, Object> rowData = objectMapper.convertValue(rootNode, Map.class);
            result.add(rowData);
        } else {
            throw new IllegalArgumentException("JSON file must contain an object or array of objects");
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