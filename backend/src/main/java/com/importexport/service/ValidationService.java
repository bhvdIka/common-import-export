package com.importexport.service;

import com.importexport.dto.ImportResponse;
import com.importexport.dto.ValidationError;
import com.importexport.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

@Service
public class ValidationService {

    @Autowired
    private CameraRepository cameraRepository;

    @Autowired
    private RobotRepository robotRepository;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MapRepository mapRepository;

    // Common validation patterns
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");
    private static final Pattern IP_PATTERN = Pattern.compile("^(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$");
    private static final Pattern ALPHANUMERIC_PATTERN = Pattern.compile("^[a-zA-Z0-9_-]+$");

    public ImportResponse validateData(List<Map<String, Object>> data, String moduleType) {
        List<ValidationError> errors = new ArrayList<>();
        int rowNum = 1; // Start from 1 (excluding header)

        for (Map<String, Object> row : data) {
            rowNum++;
            validateRow(row, moduleType, rowNum, errors);
        }

        int totalRecords = data.size();
        int failedRecords = errors.size();
        int successfulRecords = totalRecords - failedRecords;

        ImportResponse response = new ImportResponse();
        response.setStatus(errors.isEmpty() ? "VALID" : "VALIDATION_ERRORS");
        response.setMessage(errors.isEmpty() ? "All records are valid" : 
                           "Validation completed with " + errors.size() + " errors");
        response.setTotalRecords(totalRecords);
        response.setSuccessfulRecords(successfulRecords);
        response.setFailedRecords(failedRecords);
        response.setErrors(errors);

        return response;
    }

    private void validateRow(Map<String, Object> row, String moduleType, int rowNum, List<ValidationError> errors) {
        switch (moduleType.toLowerCase()) {
            case "camera":
                validateCameraRow(row, rowNum, errors);
                break;
            case "robot":
                validateRobotRow(row, rowNum, errors);
                break;
            case "task":
                validateTaskRow(row, rowNum, errors);
                break;
            case "user":
                validateUserRow(row, rowNum, errors);
                break;
            case "map":
                validateMapRow(row, rowNum, errors);
                break;
            default:
                errors.add(new ValidationError(rowNum, "moduleType", "INVALID_MODULE", 
                          "Unsupported module type: " + moduleType));
        }
    }

    private void validateCameraRow(Map<String, Object> row, int rowNum, List<ValidationError> errors) {
        // Required fields
        validateRequired(row, "name", rowNum, errors);
        validateRequired(row, "type", rowNum, errors);
        
        // Camera-specific validations
        validateCameraName(row, rowNum, errors);
        validateCameraType(row, rowNum, errors);
        validateIpAddress(row, rowNum, errors);
        validateBoolean(row, "isActive", rowNum, errors);
        
        // Business rules
        validateCameraBusinessRules(row, rowNum, errors);
    }

    private void validateRobotRow(Map<String, Object> row, int rowNum, List<ValidationError> errors) {
        // Required fields
        validateRequired(row, "name", rowNum, errors);
        validateRequired(row, "model", rowNum, errors);
        
        // Robot-specific validations
        validateRobotName(row, rowNum, errors);
        validateSerialNumber(row, rowNum, errors);
        validateManufacturer(row, rowNum, errors);
        validateBoolean(row, "isActive", rowNum, errors);
        
        // Business rules
        validateRobotBusinessRules(row, rowNum, errors);
    }

    private void validateTaskRow(Map<String, Object> row, int rowNum, List<ValidationError> errors) {
        // Required fields
        validateRequired(row, "name", rowNum, errors);
        validateRequired(row, "type", rowNum, errors);
        
        // Task-specific validations
        validateTaskName(row, rowNum, errors);
        validateTaskType(row, rowNum, errors);
        validatePriority(row, rowNum, errors);
        validateTaskStatus(row, rowNum, errors);
        validateAssignedTo(row, rowNum, errors);
        validateDueDate(row, rowNum, errors);
        validateBoolean(row, "isActive", rowNum, errors);
        
        // Business rules
        validateTaskBusinessRules(row, rowNum, errors);
    }

    private void validateUserRow(Map<String, Object> row, int rowNum, List<ValidationError> errors) {
        // Required fields
        validateRequired(row, "username", rowNum, errors);
        validateRequired(row, "email", rowNum, errors);
        
        // User-specific validations
        validateUsername(row, rowNum, errors);
        validateEmail(row, "email", rowNum, errors);
        validateUserRole(row, rowNum, errors);
        validateDepartment(row, rowNum, errors);
        validateBoolean(row, "isActive", rowNum, errors);
        
        // Business rules
        validateUserBusinessRules(row, rowNum, errors);
    }

    private void validateMapRow(Map<String, Object> row, int rowNum, List<ValidationError> errors) {
        // Required fields
        validateRequired(row, "name", rowNum, errors);
        validateRequired(row, "type", rowNum, errors);
        
        // Map-specific validations
        validateMapName(row, rowNum, errors);
        validateMapType(row, rowNum, errors);
        validateResolution(row, rowNum, errors);
        validateMapDimensions(row, rowNum, errors);
        validateCoordinates(row, rowNum, errors);
        validateBoolean(row, "isActive", rowNum, errors);
        
        // Business rules
        validateMapBusinessRules(row, rowNum, errors);
    }

    private void validateRequired(Map<String, Object> row, String field, int rowNum, List<ValidationError> errors) {
        Object value = row.get(field);
        if (value == null || value.toString().trim().isEmpty()) {
            errors.add(new ValidationError(rowNum, field, "REQUIRED", 
                      "Field '" + field + "' is required", value != null ? value.toString() : "null", "non-empty value"));
        }
    }

    private void validateEmail(Map<String, Object> row, String field, int rowNum, List<ValidationError> errors) {
        Object value = row.get(field);
        if (value != null && !value.toString().trim().isEmpty()) {
            String email = value.toString().trim();
            if (!email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")) {
                errors.add(new ValidationError(rowNum, field, "INVALID_EMAIL", 
                          "Invalid email format", email, "valid email format"));
            }
        }
    }

    private void validateBoolean(Map<String, Object> row, String field, int rowNum, List<ValidationError> errors) {
        Object value = row.get(field);
        if (value != null && !value.toString().trim().isEmpty()) {
            String boolValue = value.toString().trim().toLowerCase();
            if (!boolValue.equals("true") && !boolValue.equals("false") && 
                !boolValue.equals("1") && !boolValue.equals("0")) {
                errors.add(new ValidationError(rowNum, field, "INVALID_BOOLEAN", 
                          "Invalid boolean value", value.toString(), "true, false, 1, or 0"));
            }
        }
    }

    private void validateInteger(Map<String, Object> row, String field, int rowNum, List<ValidationError> errors) {
        Object value = row.get(field);
        if (value != null && !value.toString().trim().isEmpty()) {
            try {
                Integer.parseInt(value.toString().trim());
            } catch (NumberFormatException e) {
                errors.add(new ValidationError(rowNum, field, "INVALID_INTEGER", 
                          "Invalid integer value", value.toString(), "valid integer"));
            }
        }
    }

    private void validateDouble(Map<String, Object> row, String field, int rowNum, List<ValidationError> errors) {
        Object value = row.get(field);
        if (value != null && !value.toString().trim().isEmpty()) {
            try {
                Double.parseDouble(value.toString().trim());
            } catch (NumberFormatException e) {
                errors.add(new ValidationError(rowNum, field, "INVALID_DOUBLE", 
                          "Invalid decimal value", value.toString(), "valid decimal number"));
            }
        }
    }

    // Camera-specific validation methods
    private void validateCameraName(Map<String, Object> row, int rowNum, List<ValidationError> errors) {
        String name = getStringValue(row, "name");
        if (name != null && name.length() > 100) {
            errors.add(new ValidationError(rowNum, "name", "INVALID_LENGTH", 
                      "Camera name too long", name, "maximum 100 characters"));
        }
    }

    private void validateCameraType(Map<String, Object> row, int rowNum, List<ValidationError> errors) {
        String type = getStringValue(row, "type");
        if (type != null) {
            String[] validTypes = {"IP", "ANALOG", "PTZ", "DOME", "BULLET", "FISHEYE"};
            boolean isValid = false;
            for (String validType : validTypes) {
                if (validType.equalsIgnoreCase(type)) {
                    isValid = true;
                    break;
                }
            }
            if (!isValid) {
                errors.add(new ValidationError(rowNum, "type", "INVALID_TYPE", 
                          "Invalid camera type", type, "IP, ANALOG, PTZ, DOME, BULLET, or FISHEYE"));
            }
        }
    }

    private void validateIpAddress(Map<String, Object> row, int rowNum, List<ValidationError> errors) {
        String ipAddress = getStringValue(row, "ipAddress");
        if (ipAddress != null && !ipAddress.isEmpty() && !IP_PATTERN.matcher(ipAddress).matches()) {
            errors.add(new ValidationError(rowNum, "ipAddress", "INVALID_IP", 
                      "Invalid IP address format", ipAddress, "valid IP address (e.g., 192.168.1.1)"));
        }
    }

    private void validateCameraBusinessRules(Map<String, Object> row, int rowNum, List<ValidationError> errors) {
        String name = getStringValue(row, "name");
        String ipAddress = getStringValue(row, "ipAddress");
        
        // Check for unique camera name
        if (name != null && cameraRepository.existsByName(name)) {
            errors.add(new ValidationError(rowNum, "name", "DUPLICATE_NAME", 
                      "Camera name already exists", name, "unique camera name"));
        }
        
        // Check for unique IP address
        if (ipAddress != null && !ipAddress.isEmpty() && cameraRepository.existsByIpAddress(ipAddress)) {
            errors.add(new ValidationError(rowNum, "ipAddress", "DUPLICATE_IP", 
                      "IP address already in use", ipAddress, "unique IP address"));
        }
    }

    // Robot-specific validation methods
    private void validateRobotName(Map<String, Object> row, int rowNum, List<ValidationError> errors) {
        String name = getStringValue(row, "name");
        if (name != null && name.length() > 100) {
            errors.add(new ValidationError(rowNum, "name", "INVALID_LENGTH", 
                      "Robot name too long", name, "maximum 100 characters"));
        }
    }

    private void validateSerialNumber(Map<String, Object> row, int rowNum, List<ValidationError> errors) {
        String serialNumber = getStringValue(row, "serialNumber");
        if (serialNumber != null && !serialNumber.isEmpty()) {
            if (serialNumber.length() < 5 || serialNumber.length() > 50) {
                errors.add(new ValidationError(rowNum, "serialNumber", "INVALID_LENGTH", 
                          "Serial number length invalid", serialNumber, "5-50 characters"));
            }
            if (!ALPHANUMERIC_PATTERN.matcher(serialNumber).matches()) {
                errors.add(new ValidationError(rowNum, "serialNumber", "INVALID_FORMAT", 
                          "Serial number format invalid", serialNumber, "alphanumeric characters, hyphens, and underscores only"));
            }
        }
    }

    private void validateManufacturer(Map<String, Object> row, int rowNum, List<ValidationError> errors) {
        String manufacturer = getStringValue(row, "manufacturer");
        if (manufacturer != null && manufacturer.length() > 100) {
            errors.add(new ValidationError(rowNum, "manufacturer", "INVALID_LENGTH", 
                      "Manufacturer name too long", manufacturer, "maximum 100 characters"));
        }
    }

    private void validateRobotBusinessRules(Map<String, Object> row, int rowNum, List<ValidationError> errors) {
        String name = getStringValue(row, "name");
        String serialNumber = getStringValue(row, "serialNumber");
        
        // Check for unique robot name
        if (name != null && robotRepository.existsByName(name)) {
            errors.add(new ValidationError(rowNum, "name", "DUPLICATE_NAME", 
                      "Robot name already exists", name, "unique robot name"));
        }
        
        // Check for unique serial number
        if (serialNumber != null && !serialNumber.isEmpty() && robotRepository.existsBySerialNumber(serialNumber)) {
            errors.add(new ValidationError(rowNum, "serialNumber", "DUPLICATE_SERIAL", 
                      "Serial number already exists", serialNumber, "unique serial number"));
        }
    }

    // Task-specific validation methods
    private void validateTaskName(Map<String, Object> row, int rowNum, List<ValidationError> errors) {
        String name = getStringValue(row, "name");
        if (name != null && name.length() > 200) {
            errors.add(new ValidationError(rowNum, "name", "INVALID_LENGTH", 
                      "Task name too long", name, "maximum 200 characters"));
        }
    }

    private void validateTaskType(Map<String, Object> row, int rowNum, List<ValidationError> errors) {
        String type = getStringValue(row, "type");
        if (type != null) {
            String[] validTypes = {"MAINTENANCE", "INSPECTION", "CLEANING", "DELIVERY", "PATROL", "CUSTOM"};
            boolean isValid = false;
            for (String validType : validTypes) {
                if (validType.equalsIgnoreCase(type)) {
                    isValid = true;
                    break;
                }
            }
            if (!isValid) {
                errors.add(new ValidationError(rowNum, "type", "INVALID_TYPE", 
                          "Invalid task type", type, "MAINTENANCE, INSPECTION, CLEANING, DELIVERY, PATROL, or CUSTOM"));
            }
        }
    }

    private void validatePriority(Map<String, Object> row, int rowNum, List<ValidationError> errors) {
        Object value = row.get("priority");
        if (value != null && !value.toString().trim().isEmpty()) {
            try {
                int priority = Integer.parseInt(value.toString().trim());
                if (priority < 1 || priority > 10) {
                    errors.add(new ValidationError(rowNum, "priority", "INVALID_RANGE", 
                              "Priority out of range", value.toString(), "1-10"));
                }
            } catch (NumberFormatException e) {
                errors.add(new ValidationError(rowNum, "priority", "INVALID_INTEGER", 
                          "Invalid priority value", value.toString(), "integer between 1-10"));
            }
        }
    }

    private void validateTaskStatus(Map<String, Object> row, int rowNum, List<ValidationError> errors) {
        String status = getStringValue(row, "status");
        if (status != null) {
            String[] validStatuses = {"PENDING", "IN_PROGRESS", "COMPLETED", "CANCELLED", "ON_HOLD"};
            boolean isValid = false;
            for (String validStatus : validStatuses) {
                if (validStatus.equalsIgnoreCase(status)) {
                    isValid = true;
                    break;
                }
            }
            if (!isValid) {
                errors.add(new ValidationError(rowNum, "status", "INVALID_STATUS", 
                          "Invalid task status", status, "PENDING, IN_PROGRESS, COMPLETED, CANCELLED, or ON_HOLD"));
            }
        }
    }

    private void validateAssignedTo(Map<String, Object> row, int rowNum, List<ValidationError> errors) {
        String assignedTo = getStringValue(row, "assignedTo");
        if (assignedTo != null && assignedTo.length() > 100) {
            errors.add(new ValidationError(rowNum, "assignedTo", "INVALID_LENGTH", 
                      "Assigned to field too long", assignedTo, "maximum 100 characters"));
        }
    }

    private void validateDueDate(Map<String, Object> row, int rowNum, List<ValidationError> errors) {
        String dueDate = getStringValue(row, "dueDate");
        if (dueDate != null && !dueDate.isEmpty()) {
            // Try to parse the date to validate format
            try {
                java.time.LocalDateTime.parse(dueDate);
            } catch (Exception e) {
                errors.add(new ValidationError(rowNum, "dueDate", "INVALID_DATE", 
                          "Invalid date format", dueDate, "ISO date format (yyyy-MM-ddTHH:mm:ss)"));
            }
        }
    }

    private void validateTaskBusinessRules(Map<String, Object> row, int rowNum, List<ValidationError> errors) {
        String name = getStringValue(row, "name");
        
        // Check for unique task name
        if (name != null && taskRepository.existsByName(name)) {
            errors.add(new ValidationError(rowNum, "name", "DUPLICATE_NAME", 
                      "Task name already exists", name, "unique task name"));
        }
    }

    // User-specific validation methods
    private void validateUsername(Map<String, Object> row, int rowNum, List<ValidationError> errors) {
        String username = getStringValue(row, "username");
        if (username != null) {
            if (username.length() < 3 || username.length() > 50) {
                errors.add(new ValidationError(rowNum, "username", "INVALID_LENGTH", 
                          "Username length invalid", username, "3-50 characters"));
            }
            if (!ALPHANUMERIC_PATTERN.matcher(username).matches()) {
                errors.add(new ValidationError(rowNum, "username", "INVALID_FORMAT", 
                          "Username format invalid", username, "alphanumeric characters, hyphens, and underscores only"));
            }
        }
    }

    private void validateUserRole(Map<String, Object> row, int rowNum, List<ValidationError> errors) {
        String role = getStringValue(row, "role");
        if (role != null) {
            String[] validRoles = {"ADMIN", "MANAGER", "OPERATOR", "VIEWER", "TECHNICIAN"};
            boolean isValid = false;
            for (String validRole : validRoles) {
                if (validRole.equalsIgnoreCase(role)) {
                    isValid = true;
                    break;
                }
            }
            if (!isValid) {
                errors.add(new ValidationError(rowNum, "role", "INVALID_ROLE", 
                          "Invalid user role", role, "ADMIN, MANAGER, OPERATOR, VIEWER, or TECHNICIAN"));
            }
        }
    }

    private void validateDepartment(Map<String, Object> row, int rowNum, List<ValidationError> errors) {
        String department = getStringValue(row, "department");
        if (department != null && department.length() > 100) {
            errors.add(new ValidationError(rowNum, "department", "INVALID_LENGTH", 
                      "Department name too long", department, "maximum 100 characters"));
        }
    }

    private void validateUserBusinessRules(Map<String, Object> row, int rowNum, List<ValidationError> errors) {
        String username = getStringValue(row, "username");
        String email = getStringValue(row, "email");
        
        // Check for unique username
        if (username != null && userRepository.existsByUsername(username)) {
            errors.add(new ValidationError(rowNum, "username", "DUPLICATE_USERNAME", 
                      "Username already exists", username, "unique username"));
        }
        
        // Check for unique email
        if (email != null && userRepository.existsByEmail(email)) {
            errors.add(new ValidationError(rowNum, "email", "DUPLICATE_EMAIL", 
                      "Email already exists", email, "unique email address"));
        }
    }

    // Map-specific validation methods
    private void validateMapName(Map<String, Object> row, int rowNum, List<ValidationError> errors) {
        String name = getStringValue(row, "name");
        if (name != null && name.length() > 100) {
            errors.add(new ValidationError(rowNum, "name", "INVALID_LENGTH", 
                      "Map name too long", name, "maximum 100 characters"));
        }
    }

    private void validateMapType(Map<String, Object> row, int rowNum, List<ValidationError> errors) {
        String type = getStringValue(row, "type");
        if (type != null) {
            String[] validTypes = {"FLOOR_PLAN", "OCCUPANCY_GRID", "TOPOLOGICAL", "HYBRID", "3D_POINT_CLOUD"};
            boolean isValid = false;
            for (String validType : validTypes) {
                if (validType.equalsIgnoreCase(type)) {
                    isValid = true;
                    break;
                }
            }
            if (!isValid) {
                errors.add(new ValidationError(rowNum, "type", "INVALID_TYPE", 
                          "Invalid map type", type, "FLOOR_PLAN, OCCUPANCY_GRID, TOPOLOGICAL, HYBRID, or 3D_POINT_CLOUD"));
            }
        }
    }

    private void validateResolution(Map<String, Object> row, int rowNum, List<ValidationError> errors) {
        Object value = row.get("resolution");
        if (value != null && !value.toString().trim().isEmpty()) {
            try {
                double resolution = Double.parseDouble(value.toString().trim());
                if (resolution <= 0 || resolution > 10) {
                    errors.add(new ValidationError(rowNum, "resolution", "INVALID_RANGE", 
                              "Resolution out of range", value.toString(), "0.001-10.0 meters/pixel"));
                }
            } catch (NumberFormatException e) {
                errors.add(new ValidationError(rowNum, "resolution", "INVALID_DOUBLE", 
                          "Invalid resolution value", value.toString(), "decimal number"));
            }
        }
    }

    private void validateMapDimensions(Map<String, Object> row, int rowNum, List<ValidationError> errors) {
        validatePositiveInteger(row, "width", rowNum, errors, 10000);
        validatePositiveInteger(row, "height", rowNum, errors, 10000);
    }

    private void validateCoordinates(Map<String, Object> row, int rowNum, List<ValidationError> errors) {
        validateCoordinate(row, "originX", rowNum, errors);
        validateCoordinate(row, "originY", rowNum, errors);
    }

    private void validateMapBusinessRules(Map<String, Object> row, int rowNum, List<ValidationError> errors) {
        String name = getStringValue(row, "name");
        
        // Check for unique map name
        if (name != null && mapRepository.existsByName(name)) {
            errors.add(new ValidationError(rowNum, "name", "DUPLICATE_NAME", 
                      "Map name already exists", name, "unique map name"));
        }
    }

    // Helper methods
    private String getStringValue(Map<String, Object> row, String key) {
        Object value = row.get(key);
        return value != null ? value.toString().trim() : null;
    }

    private void validatePositiveInteger(Map<String, Object> row, String field, int rowNum, List<ValidationError> errors, int maxValue) {
        Object value = row.get(field);
        if (value != null && !value.toString().trim().isEmpty()) {
            try {
                int intValue = Integer.parseInt(value.toString().trim());
                if (intValue <= 0 || intValue > maxValue) {
                    errors.add(new ValidationError(rowNum, field, "INVALID_RANGE", 
                              field + " out of range", value.toString(), "1-" + maxValue));
                }
            } catch (NumberFormatException e) {
                errors.add(new ValidationError(rowNum, field, "INVALID_INTEGER", 
                          "Invalid " + field + " value", value.toString(), "positive integer"));
            }
        }
    }

    private void validateCoordinate(Map<String, Object> row, String field, int rowNum, List<ValidationError> errors) {
        Object value = row.get(field);
        if (value != null && !value.toString().trim().isEmpty()) {
            try {
                double coordinate = Double.parseDouble(value.toString().trim());
                if (coordinate < -1000000 || coordinate > 1000000) {
                    errors.add(new ValidationError(rowNum, field, "INVALID_RANGE", 
                              field + " coordinate out of range", value.toString(), "-1,000,000 to 1,000,000"));
                }
            } catch (NumberFormatException e) {
                errors.add(new ValidationError(rowNum, field, "INVALID_DOUBLE", 
                          "Invalid coordinate value", value.toString(), "decimal number"));
            }
        }
    }
}