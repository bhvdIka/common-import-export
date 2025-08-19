package com.importexport.service;

import com.importexport.dto.ImportResponse;
import com.importexport.dto.ValidationError;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class ValidationService {

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
        validateRequired(row, "name", rowNum, errors);
        validateRequired(row, "type", rowNum, errors);
        validateBoolean(row, "isActive", rowNum, errors);
    }

    private void validateRobotRow(Map<String, Object> row, int rowNum, List<ValidationError> errors) {
        validateRequired(row, "name", rowNum, errors);
        validateRequired(row, "model", rowNum, errors);
        validateBoolean(row, "isActive", rowNum, errors);
    }

    private void validateTaskRow(Map<String, Object> row, int rowNum, List<ValidationError> errors) {
        validateRequired(row, "name", rowNum, errors);
        validateRequired(row, "type", rowNum, errors);
        validateInteger(row, "priority", rowNum, errors);
        validateBoolean(row, "isActive", rowNum, errors);
    }

    private void validateUserRow(Map<String, Object> row, int rowNum, List<ValidationError> errors) {
        validateRequired(row, "username", rowNum, errors);
        validateRequired(row, "email", rowNum, errors);
        validateEmail(row, "email", rowNum, errors);
        validateBoolean(row, "isActive", rowNum, errors);
    }

    private void validateMapRow(Map<String, Object> row, int rowNum, List<ValidationError> errors) {
        validateRequired(row, "name", rowNum, errors);
        validateRequired(row, "type", rowNum, errors);
        validateDouble(row, "resolution", rowNum, errors);
        validateBoolean(row, "isActive", rowNum, errors);
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
}