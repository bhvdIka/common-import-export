# Camera Field Mapping

## Overview
This document describes the field mappings for Camera module import/export operations.

## Required Fields
- `name`: Camera display name
- `type`: Camera type/category

## Optional Fields
- `ipAddress`: IP address for network cameras
- `location`: Physical location description
- `isActive`: Active status (default: true)
- `description`: Additional description

## Field Details

### name
- **Type**: String
- **Required**: Yes
- **Max Length**: 100 characters
- **Validation**: Must not be empty
- **Example**: "Security Camera 1"

### type
- **Type**: String
- **Required**: Yes
- **Max Length**: 50 characters
- **Valid Values**: IP Camera, PTZ Camera, Dome Camera, Bullet Camera, Thermal Camera, Trail Camera
- **Example**: "IP Camera"

### ipAddress
- **Type**: String
- **Required**: No
- **Format**: IPv4 address (xxx.xxx.xxx.xxx)
- **Validation**: Must be valid IP address format if provided
- **Example**: "192.168.1.100"

### location
- **Type**: String
- **Required**: No
- **Max Length**: 200 characters
- **Example**: "Main Entrance"

### isActive
- **Type**: Boolean
- **Required**: No
- **Default**: true
- **Valid Values**: true, false, 1, 0, yes, no (case insensitive)
- **Example**: true

### description
- **Type**: String
- **Required**: No
- **Max Length**: 1000 characters
- **Example**: "Primary entrance security monitoring system"

## CSV Format Example

```csv
name,type,ip_address,location,is_active,description
Security Camera 1,IP Camera,192.168.1.100,Main Entrance,true,Primary entrance monitoring
PTZ Camera 2,PTZ Camera,192.168.1.101,Parking Lot,true,360-degree surveillance
```

## JSON Format Example

```json
[
  {
    "name": "Security Camera 1",
    "type": "IP Camera",
    "ipAddress": "192.168.1.100",
    "location": "Main Entrance",
    "isActive": true,
    "description": "Primary entrance monitoring"
  }
]
```

## Excel Format
- Header row must be present
- Use the same field names as CSV
- Boolean values can be TRUE/FALSE or 1/0
- Empty cells are treated as null values

## Validation Rules

### Business Rules
1. Camera names should be unique within the system
2. IP addresses must be unique if provided
3. Active cameras should have valid IP addresses for network types

### Data Quality Guidelines
1. Use descriptive names that indicate camera purpose and location
2. Include location information for easier identification
3. Provide accurate IP addresses for network cameras
4. Use consistent naming conventions (e.g., "Camera 1", "Camera 2")

## Common Import Issues

### Validation Errors
- **Missing name**: Ensure all cameras have names
- **Invalid IP address**: Check IP format (192.168.1.100)
- **Invalid boolean**: Use true/false, 1/0, or yes/no
- **Name too long**: Keep names under 100 characters

### Data Quality Issues
- **Duplicate names**: Each camera should have unique name
- **Inconsistent types**: Standardize camera type names
- **Missing locations**: Add location for better identification
- **Incomplete IP addresses**: Provide full IP for network cameras

## Export Options

### Available Fields
All fields listed above can be included in exports.

### Default Export Fields
When no specific fields are selected, all fields are exported:
- id, name, type, ipAddress, location, isActive, description

### Filtering
- Filter by active status: `isActive = true`
- Filter by type: `type = 'IP Camera'`
- Filter by location: `location LIKE '%Entrance%'`

## Integration Notes

### Database Mapping
- Maps to `cameras` table
- Primary key: `id` (auto-generated)
- Unique constraints: `name`
- Indexes: `name`, `type`, `location`

### API Endpoints
- Import: `POST /api/camera/import`
- Export: `POST /api/camera/export`
- Template: `GET /api/camera/template`