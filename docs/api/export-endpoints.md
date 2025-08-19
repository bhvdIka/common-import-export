# Export API Endpoints

## Base URL
All export endpoints are relative to `/api/{module}/export` where `{module}` is one of: `camera`, `robot`, `task`, `user`, `map`.

## Export Data

### POST /{module}/export

Export data to specified format.

#### Request
- **Content-Type**: `application/json`

```json
{
  "fileFormat": "csv|xlsx|json",
  "fields": ["id", "name", "type", "description"],
  "filter": "isActive = true",
  "sortBy": "name",
  "sortOrder": "ASC|DESC",
  "includeInactive": false
}
```

#### Parameters
- `fileFormat` (required): Output format (csv, xlsx, json)
- `fields` (optional): Specific fields to include. If empty, all fields are included
- `filter` (optional): SQL-like filter expression
- `sortBy` (optional): Field to sort by
- `sortOrder` (optional): Sort direction (ASC or DESC, default: ASC)
- `includeInactive` (optional): Include inactive records (default: false)

#### Response
- **Content-Type**: `application/octet-stream`
- **Headers**: 
  - `Content-Disposition: attachment; filename="{module}_export.{format}"`
- **Body**: Binary file content

#### Status Codes
- **200 OK**: Export successful, file content in response body
- **400 Bad Request**: Invalid request parameters
- **404 Not Found**: No data found matching criteria
- **500 Internal Server Error**: Server error during export

## Get Template

### GET /{module}/template

Download template file for the module.

#### Request
- **Parameters**:
  - `format` (optional, default: csv): Template format (csv, xlsx)

#### Response
- **Content-Type**: `application/octet-stream`
- **Headers**: 
  - `Content-Disposition: attachment; filename="{module}_template.{format}"`
- **Body**: Template file content

#### Status Codes
- **200 OK**: Template downloaded successfully
- **400 Bad Request**: Invalid format parameter
- **404 Not Found**: Template not found
- **500 Internal Server Error**: Server error

## Filter Syntax

The filter parameter supports SQL-like expressions:

### Operators
- **Comparison**: `=`, `!=`, `<`, `<=`, `>`, `>=`
- **Logical**: `AND`, `OR`, `NOT`
- **Pattern Matching**: `LIKE` (with `%` wildcards)
- **Null Checks**: `IS NULL`, `IS NOT NULL`
- **Lists**: `IN (value1, value2, ...)`

### Examples
```sql
-- Basic comparison
isActive = true

-- Multiple conditions
isActive = true AND type = 'IP Camera'

-- Pattern matching
name LIKE 'Camera%'

-- Date ranges (use ISO format)
createdAt >= '2024-01-01' AND createdAt < '2024-02-01'

-- List membership
status IN ('Active', 'Pending', 'Processing')

-- Null checks
description IS NOT NULL
```

## Available Fields by Module

### Camera
- `id`, `name`, `type`, `ipAddress`, `location`, `isActive`, `description`

### Robot
- `id`, `name`, `model`, `serialNumber`, `manufacturer`, `isActive`, `description`

### Task
- `id`, `name`, `type`, `priority`, `status`, `assignedTo`, `dueDate`, `isActive`, `description`

### User
- `id`, `username`, `email`, `firstName`, `lastName`, `role`, `department`, `isActive`

### Map
- `id`, `name`, `type`, `resolution`, `width`, `height`, `originX`, `originY`, `isActive`, `description`

## Examples

### Export All Active Cameras as CSV
```bash
curl -X POST http://localhost:8080/api/camera/export \
  -H "Content-Type: application/json" \
  -d '{
    "fileFormat": "csv",
    "filter": "isActive = true",
    "sortBy": "name"
  }' \
  --output cameras.csv
```

### Export Specific User Fields as Excel
```bash
curl -X POST http://localhost:8080/api/user/export \
  -H "Content-Type: application/json" \
  -d '{
    "fileFormat": "xlsx",
    "fields": ["username", "email", "firstName", "lastName", "role"],
    "filter": "isActive = true",
    "sortBy": "lastName"
  }' \
  --output users.xlsx
```

### Download Camera Template
```bash
curl -X GET "http://localhost:8080/api/camera/template?format=csv" \
  --output camera_template.csv
```