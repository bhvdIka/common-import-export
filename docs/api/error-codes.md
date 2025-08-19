# Error Codes Documentation

## Overview
This document describes all error codes used in the Import/Export system, their meanings, and recommended actions.

## Validation Error Codes

### REQUIRED
**Description**: A required field is missing or empty.
**Trigger**: When a mandatory field has no value.
**Resolution**: Provide a value for the required field.
**Example**: 
- Field: `name`
- Message: "Field 'name' is required"

### INVALID_EMAIL
**Description**: Email address format is invalid.
**Trigger**: When email field doesn't match valid email pattern.
**Resolution**: Provide a valid email address (e.g., user@domain.com).
**Example**:
- Field: `email`
- Actual Value: "invalid-email"
- Expected Value: "valid email format"

### INVALID_BOOLEAN
**Description**: Boolean field has invalid value.
**Trigger**: When boolean field contains values other than true/false/1/0/yes/no.
**Resolution**: Use valid boolean values.
**Example**:
- Field: `isActive`
- Actual Value: "maybe"
- Expected Value: "true, false, 1, or 0"

### INVALID_INTEGER
**Description**: Integer field contains non-numeric value.
**Trigger**: When integer field cannot be parsed as a whole number.
**Resolution**: Provide a valid integer value.
**Example**:
- Field: `priority`
- Actual Value: "high"
- Expected Value: "valid integer"

### INVALID_DOUBLE
**Description**: Decimal field contains non-numeric value.
**Trigger**: When decimal field cannot be parsed as a number.
**Resolution**: Provide a valid decimal number.
**Example**:
- Field: `resolution`
- Actual Value: "high"
- Expected Value: "valid decimal number"

### INVALID_DATE
**Description**: Date field has invalid format.
**Trigger**: When date field cannot be parsed as a valid date.
**Resolution**: Use ISO format (YYYY-MM-DD HH:mm:ss) or other supported formats.
**Example**:
- Field: `dueDate`
- Actual Value: "tomorrow"
- Expected Value: "2024-01-15 14:30:00"

### INVALID_IP_ADDRESS
**Description**: IP address format is invalid.
**Trigger**: When IP address field doesn't match IPv4 pattern.
**Resolution**: Provide a valid IPv4 address.
**Example**:
- Field: `ipAddress`
- Actual Value: "192.168.1"
- Expected Value: "192.168.1.100"

### DUPLICATE_VALUE
**Description**: Value already exists where uniqueness is required.
**Trigger**: When a unique field (like username, email) has duplicate value.
**Resolution**: Use a unique value that doesn't already exist.
**Example**:
- Field: `username`
- Actual Value: "john.doe"
- Expected Value: "unique username"

### INVALID_REFERENCE
**Description**: Foreign key reference doesn't exist.
**Trigger**: When referenced entity doesn't exist in the system.
**Resolution**: Use a valid reference ID that exists in the system.
**Example**:
- Field: `assignedTo`
- Actual Value: "nonexistent.user"
- Expected Value: "valid username"

### INVALID_LENGTH
**Description**: Field value length is outside allowed range.
**Trigger**: When field is too short or too long.
**Resolution**: Adjust field length to meet requirements.
**Example**:
- Field: `description`
- Actual Value: "Very long description that exceeds..."
- Expected Value: "maximum 1000 characters"

### INVALID_RANGE
**Description**: Numeric value is outside allowed range.
**Trigger**: When numeric field is below minimum or above maximum.
**Resolution**: Use a value within the allowed range.
**Example**:
- Field: `priority`
- Actual Value: "15"
- Expected Value: "1-10"

## System Error Codes

### FILE_TOO_LARGE
**Description**: Uploaded file exceeds size limit.
**HTTP Status**: 413 Payload Too Large
**Resolution**: Reduce file size or split into smaller files.

### UNSUPPORTED_FORMAT
**Description**: File format is not supported.
**HTTP Status**: 415 Unsupported Media Type
**Resolution**: Use supported formats: CSV, Excel (.xlsx/.xls), JSON.

### FILE_PROCESSING_ERROR
**Description**: Error occurred while processing the file.
**HTTP Status**: 500 Internal Server Error
**Resolution**: Check file format and content, retry with valid file.

### TEMPLATE_NOT_FOUND
**Description**: Template file not found for the module.
**HTTP Status**: 404 Not Found
**Resolution**: Ensure template exists for the requested module and format.

### PERMISSION_DENIED
**Description**: User doesn't have permission for the operation.
**HTTP Status**: 403 Forbidden
**Resolution**: Contact administrator for required permissions.

### INVALID_MODULE
**Description**: Module type is not supported.
**HTTP Status**: 400 Bad Request
**Resolution**: Use supported modules: camera, robot, task, user, map.

## Error Response Format

All validation errors follow this structure:

```json
{
  "row": 5,
  "field": "email",
  "errorCode": "INVALID_EMAIL",
  "errorMessage": "Invalid email format",
  "actualValue": "invalid-email",
  "expectedValue": "valid email format"
}
```

## Error Handling Best Practices

### For Developers
1. Always check the `errorCode` field for programmatic handling
2. Display `errorMessage` to users for human-readable information
3. Use `row` information to highlight problematic records
4. Provide suggestions based on `expectedValue`

### For Users
1. Review all validation errors before re-importing
2. Use templates to ensure correct format
3. Validate data in smaller batches for easier error identification
4. Keep backups of original data before importing

### Common Fixes
1. **Email errors**: Ensure @ symbol and valid domain
2. **Boolean errors**: Use true/false, 1/0, or yes/no
3. **Date errors**: Use YYYY-MM-DD or YYYY-MM-DD HH:mm:ss format
4. **Number errors**: Remove non-numeric characters
5. **Required errors**: Ensure all mandatory fields have values

## Troubleshooting

### High Error Count
- Check file format matches template
- Verify data source quality
- Consider data cleaning before import

### Specific Field Errors
- Review field mapping documentation
- Check data type requirements
- Validate against business rules

### Performance Issues
- Reduce batch size for large files
- Split files into smaller chunks
- Process during off-peak hours