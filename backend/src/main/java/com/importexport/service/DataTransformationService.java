package com.importexport.service;

import com.importexport.entity.*;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Map;

@Service
public class DataTransformationService {

    private static final DateTimeFormatter[] DATE_FORMATTERS = {
        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"),
        DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss"),
        DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"),
        DateTimeFormatter.ofPattern("MM/dd/yyyy HH:mm:ss"),
        DateTimeFormatter.ofPattern("yyyy-MM-dd"),
        DateTimeFormatter.ISO_LOCAL_DATE_TIME
    };

    public Camera transformToCamera(Map<String, Object> row) {
        Camera camera = new Camera();
        camera.setName(getStringValue(row, "name"));
        camera.setType(getStringValue(row, "type"));
        camera.setIpAddress(getStringValue(row, "ipAddress"));
        camera.setLocation(getStringValue(row, "location"));
        camera.setDescription(getStringValue(row, "description"));
        camera.setIsActive(getBooleanValue(row, "isActive", true));
        return camera;
    }

    public Robot transformToRobot(Map<String, Object> row) {
        Robot robot = new Robot();
        robot.setName(getStringValue(row, "name"));
        robot.setModel(getStringValue(row, "model"));
        robot.setSerialNumber(getStringValue(row, "serialNumber"));
        robot.setManufacturer(getStringValue(row, "manufacturer"));
        robot.setDescription(getStringValue(row, "description"));
        robot.setIsActive(getBooleanValue(row, "isActive", true));
        return robot;
    }

    public Task transformToTask(Map<String, Object> row) {
        Task task = new Task();
        task.setName(getStringValue(row, "name"));
        task.setType(getStringValue(row, "type"));
        task.setPriority(getIntegerValue(row, "priority"));
        task.setStatus(getStringValue(row, "status"));
        task.setAssignedTo(getStringValue(row, "assignedTo"));
        task.setDescription(getStringValue(row, "description"));
        task.setIsActive(getBooleanValue(row, "isActive", true));
        
        // Handle due date conversion
        String dueDateStr = getStringValue(row, "dueDate");
        if (dueDateStr != null && !dueDateStr.trim().isEmpty()) {
            task.setDueDate(parseDateTime(dueDateStr));
        }
        
        return task;
    }

    public User transformToUser(Map<String, Object> row) {
        User user = new User();
        user.setUsername(getStringValue(row, "username"));
        user.setEmail(getStringValue(row, "email"));
        user.setFirstName(getStringValue(row, "firstName"));
        user.setLastName(getStringValue(row, "lastName"));
        user.setRole(getStringValue(row, "role"));
        user.setDepartment(getStringValue(row, "department"));
        user.setIsActive(getBooleanValue(row, "isActive", true));
        return user;
    }

    public com.importexport.entity.Map transformToMap(java.util.Map<String, Object> row) {
        com.importexport.entity.Map map = new com.importexport.entity.Map();
        map.setName(getStringValue(row, "name"));
        map.setType(getStringValue(row, "type"));
        map.setResolution(getDoubleValue(row, "resolution"));
        map.setWidth(getIntegerValue(row, "width"));
        map.setHeight(getIntegerValue(row, "height"));
        map.setOriginX(getDoubleValue(row, "originX"));
        map.setOriginY(getDoubleValue(row, "originY"));
        map.setDescription(getStringValue(row, "description"));
        map.setIsActive(getBooleanValue(row, "isActive", true));
        return map;
    }

    private String getStringValue(Map<String, Object> row, String key) {
        Object value = row.get(key);
        return value != null ? value.toString().trim() : null;
    }

    private Boolean getBooleanValue(Map<String, Object> row, String key, Boolean defaultValue) {
        Object value = row.get(key);
        if (value == null || value.toString().trim().isEmpty()) {
            return defaultValue;
        }
        
        String strValue = value.toString().trim().toLowerCase();
        if ("true".equals(strValue) || "1".equals(strValue) || "yes".equals(strValue)) {
            return true;
        } else if ("false".equals(strValue) || "0".equals(strValue) || "no".equals(strValue)) {
            return false;
        }
        
        return defaultValue;
    }

    private Integer getIntegerValue(Map<String, Object> row, String key) {
        Object value = row.get(key);
        if (value == null || value.toString().trim().isEmpty()) {
            return null;
        }
        
        try {
            return Integer.parseInt(value.toString().trim());
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private Double getDoubleValue(Map<String, Object> row, String key) {
        Object value = row.get(key);
        if (value == null || value.toString().trim().isEmpty()) {
            return null;
        }
        
        try {
            return Double.parseDouble(value.toString().trim());
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private LocalDateTime parseDateTime(String dateTimeStr) {
        if (dateTimeStr == null || dateTimeStr.trim().isEmpty()) {
            return null;
        }
        
        for (DateTimeFormatter formatter : DATE_FORMATTERS) {
            try {
                return LocalDateTime.parse(dateTimeStr.trim(), formatter);
            } catch (DateTimeParseException e) {
                // Try next formatter
            }
        }
        
        return null; // Could not parse with any formatter
    }

    public void updateFieldMapping(Map<String, Object> row, Map<String, String> fieldMappings) {
        if (fieldMappings != null && !fieldMappings.isEmpty()) {
            Map<String, Object> updatedRow = new java.util.HashMap<>();
            for (Map.Entry<String, Object> entry : row.entrySet()) {
                String originalKey = entry.getKey();
                String mappedKey = fieldMappings.getOrDefault(originalKey, originalKey);
                updatedRow.put(mappedKey, entry.getValue());
            }
            row.clear();
            row.putAll(updatedRow);
        }
    }

    public String sanitizeString(String input) {
        if (input == null) {
            return null;
        }
        
        // Remove potential XSS and injection attempts
        return input.trim()
                   .replaceAll("<script[^>]*>.*?</script>", "")
                   .replaceAll("<[^>]*>", "")
                   .replaceAll("javascript:", "")
                   .replaceAll("vbscript:", "")
                   .replaceAll("onload", "")
                   .replaceAll("onerror", "")
                   .replaceAll("onclick", "");
    }
}