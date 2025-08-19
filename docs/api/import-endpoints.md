# Import API Endpoints

## Base URL
All import endpoints are relative to `/api/{module}/import` where `{module}` is one of: `camera`, `robot`, `task`, `user`, `map`.

## Import Data

### POST /{module}/import

Import data from uploaded file.

#### Request
- **Content-Type**: `multipart/form-data`
- **Parameters**:
  - `file` (required): The file to import (CSV, Excel, or JSON)
  - `validateOnly` (optional, default: false): If true, only validate the data without importing
  - `skipErrors` (optional, default: false): If true, skip invalid records and continue processing
  - `batchSize` (optional, default: 100): Batch size for processing records

#### Response
```json
{
  "status": "SUCCESS|ERROR|VALIDATION_ERRORS|VALID",
  "message": "Human readable message",
  "totalRecords": 100,
  "successfulRecords": 95,
  "failedRecords": 5,
  "errors": [
    {
      "row": 10,
      "field": "email",
      "errorCode": "INVALID_EMAIL",
      "errorMessage": "Invalid email format",
      "actualValue": "invalid-email",
      "expectedValue": "valid email format"
    }
  ],
  "processedAt": "2024-01-15T10:30:00Z",
  "processingTimeMs": 1500
}
```

#### Status Codes
- **200 OK**: Import completed (check response status for details)
- **400 Bad Request**: Invalid request parameters or file format
- **413 Payload Too Large**: File size exceeds limit
- **415 Unsupported Media Type**: Unsupported file format
- **500 Internal Server Error**: Server error during processing

## Supported File Formats

### CSV Format
- **Extensions**: `.csv`
- **Encoding**: UTF-8
- **Delimiter**: Comma (`,`)
- **Quote Character**: Double quote (`"`)
- **Header Row**: Required

### Excel Format
- **Extensions**: `.xlsx`, `.xls`
- **Sheet**: First sheet is processed
- **Header Row**: Required (first row)

### JSON Format
- **Extension**: `.json`
- **Structure**: Array of objects or single object
- **Encoding**: UTF-8

## File Size Limits
- **Development**: 5MB
- **Production**: 50MB

## Error Codes

| Code | Description |
|------|-------------|
| `REQUIRED` | Required field is missing |
| `INVALID_EMAIL` | Invalid email format |
| `INVALID_BOOLEAN` | Invalid boolean value |
| `INVALID_INTEGER` | Invalid integer value |
| `INVALID_DOUBLE` | Invalid decimal value |
| `INVALID_DATE` | Invalid date format |
| `DUPLICATE_VALUE` | Duplicate value where unique required |
| `INVALID_REFERENCE` | Invalid foreign key reference |

## Examples

### Import Camera Data
```bash
curl -X POST http://localhost:8080/api/camera/import \
  -F "file=@cameras.csv" \
  -F "validateOnly=false" \
  -F "skipErrors=true"
```

### Validate User Data Only
```bash
curl -X POST http://localhost:8080/api/user/import \
  -F "file=@users.xlsx" \
  -F "validateOnly=true"
```