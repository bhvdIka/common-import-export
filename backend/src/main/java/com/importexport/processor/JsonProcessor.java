package com.importexport.processor;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.util.*;

@Component
public class JsonProcessor implements FileProcessor {

    private static final String[] SUPPORTED_EXTENSIONS = {"json"};
    private final ObjectMapper objectMapper;

    public JsonProcessor() {
        this.objectMapper = new ObjectMapper();
        // Configure ObjectMapper for better error handling
        objectMapper.configure(com.fasterxml.jackson.core.JsonParser.Feature.ALLOW_COMMENTS, true);
        objectMapper.configure(com.fasterxml.jackson.core.JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);
        objectMapper.configure(com.fasterxml.jackson.core.JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
        objectMapper.configure(com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    @Override
    public List<Map<String, Object>> processFile(InputStream inputStream) throws Exception {
        List<Map<String, Object>> result = new ArrayList<>();
        
        try {
            JsonNode rootNode = objectMapper.readTree(inputStream);
            
            if (rootNode.isArray()) {
                // Handle array of objects
                result = processArrayNode((ArrayNode) rootNode);
            } else if (rootNode.isObject()) {
                // Handle single object or nested structure
                result = processObjectNode((ObjectNode) rootNode);
            } else {
                throw new IllegalArgumentException("JSON file must contain an object or array of objects");
            }
            
            // Validate that we have data
            if (result.isEmpty()) {
                throw new IllegalArgumentException("JSON file contains no valid data records");
            }
            
            return result;
            
        } catch (JsonProcessingException e) {
            throw new Exception("Invalid JSON format: " + e.getMessage(), e);
        } catch (Exception e) {
            throw new Exception("Error processing JSON file: " + e.getMessage(), e);
        }
    }
    
    private List<Map<String, Object>> processArrayNode(ArrayNode arrayNode) throws Exception {
        List<Map<String, Object>> result = new ArrayList<>();
        
        for (int i = 0; i < arrayNode.size(); i++) {
            JsonNode node = arrayNode.get(i);
            if (node.isObject()) {
                Map<String, Object> rowData = flattenJsonObject((ObjectNode) node, "");
                if (!rowData.isEmpty()) {
                    result.add(rowData);
                }
            } else {
                // Handle primitive values in array
                Map<String, Object> rowData = new LinkedHashMap<>();
                rowData.put("value", convertJsonValue(node));
                rowData.put("index", i);
                result.add(rowData);
            }
        }
        
        return result;
    }
    
    private List<Map<String, Object>> processObjectNode(ObjectNode objectNode) throws Exception {
        List<Map<String, Object>> result = new ArrayList<>();
        
        // Check if this object contains an array field that should be the main data
        for (Iterator<Map.Entry<String, JsonNode>> fields = objectNode.fields(); fields.hasNext(); ) {
            Map.Entry<String, JsonNode> field = fields.next();
            JsonNode value = field.getValue();
            
            if (value.isArray() && value.size() > 0) {
                // This looks like a data array, process it
                List<Map<String, Object>> arrayResult = processArrayNode((ArrayNode) value);
                if (!arrayResult.isEmpty()) {
                    return arrayResult; // Return the array data
                }
            }
        }
        
        // If no array found, treat the object as a single record
        Map<String, Object> rowData = flattenJsonObject(objectNode, "");
        result.add(rowData);
        
        return result;
    }
    
    private Map<String, Object> flattenJsonObject(ObjectNode objectNode, String prefix) {
        Map<String, Object> result = new LinkedHashMap<>();
        
        for (Iterator<Map.Entry<String, JsonNode>> fields = objectNode.fields(); fields.hasNext(); ) {
            Map.Entry<String, JsonNode> field = fields.next();
            String fieldName = prefix.isEmpty() ? field.getKey() : prefix + "." + field.getKey();
            JsonNode value = field.getValue();
            
            if (value.isObject() && !value.isEmpty()) {
                // Recursively flatten nested objects
                Map<String, Object> nestedResult = flattenJsonObject((ObjectNode) value, fieldName);
                result.putAll(nestedResult);
            } else if (value.isArray() && value.size() > 0) {
                // Handle arrays - convert to comma-separated string for simple values
                if (isSimpleArray(value)) {
                    List<String> arrayValues = new ArrayList<>();
                    for (JsonNode arrayElement : value) {
                        arrayValues.add(String.valueOf(convertJsonValue(arrayElement)));
                    }
                    result.put(fieldName, String.join(", ", arrayValues));
                } else {
                    // For complex arrays, just put the first element or indicate array
                    result.put(fieldName, "[Array with " + value.size() + " elements]");
                }
            } else {
                // Handle primitive values
                result.put(fieldName, convertJsonValue(value));
            }
        }
        
        return result;
    }
    
    private boolean isSimpleArray(JsonNode arrayNode) {
        for (JsonNode element : arrayNode) {
            if (element.isObject() || element.isArray()) {
                return false;
            }
        }
        return true;
    }
    
    private Object convertJsonValue(JsonNode node) {
        if (node == null || node.isNull()) {
            return null;
        }
        
        if (node.isBoolean()) {
            return node.booleanValue();
        }
        
        if (node.isInt()) {
            return node.intValue();
        }
        
        if (node.isLong()) {
            return node.longValue();
        }
        
        if (node.isFloat()) {
            return node.floatValue();
        }
        
        if (node.isDouble()) {
            return node.doubleValue();
        }
        
        if (node.isTextual()) {
            String textValue = node.textValue();
            
            // Try to detect and convert common patterns
            if (textValue != null) {
                String trimmed = textValue.trim();
                
                // Check for boolean strings
                if ("true".equalsIgnoreCase(trimmed) || "false".equalsIgnoreCase(trimmed)) {
                    return Boolean.parseBoolean(trimmed);
                }
                
                // Check for numeric strings
                if (trimmed.matches("-?\\d+")) {
                    try {
                        return Integer.parseInt(trimmed);
                    } catch (NumberFormatException e) {
                        try {
                            return Long.parseLong(trimmed);
                        } catch (NumberFormatException e2) {
                            // Fall back to string
                        }
                    }
                }
                
                if (trimmed.matches("-?\\d*\\.\\d+")) {
                    try {
                        return Double.parseDouble(trimmed);
                    } catch (NumberFormatException e) {
                        // Fall back to string
                    }
                }
                
                // Check for null string
                if ("null".equalsIgnoreCase(trimmed)) {
                    return null;
                }
            }
            
            return textValue;
        }
        
        // For other types, convert to string
        return node.toString();
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