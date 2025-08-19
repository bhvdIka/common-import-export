# User Field Mapping

## Overview
This document describes the field mappings for User module import/export operations.

## Required Fields
- `username`: Unique user identifier
- `email`: User email address

## Optional Fields
- `firstName`: User's first name
- `lastName`: User's last name
- `role`: User role in the system
- `department`: User's department
- `isActive`: Active status (default: true)

## Field Details

### username
- **Type**: String
- **Required**: Yes
- **Max Length**: 50 characters
- **Validation**: Must be unique, alphanumeric with dots/underscores allowed
- **Example**: "john.doe"

### email
- **Type**: String
- **Required**: Yes
- **Max Length**: 100 characters
- **Validation**: Must be valid email format and unique
- **Example**: "john.doe@company.com"

### firstName
- **Type**: String
- **Required**: No
- **Max Length**: 50 characters
- **Example**: "John"

### lastName
- **Type**: String
- **Required**: No
- **Max Length**: 50 characters
- **Example**: "Doe"

### role
- **Type**: String
- **Required**: No
- **Max Length**: 50 characters
- **Valid Values**: Administrator, Operator, Supervisor, Analyst, Technician
- **Example**: "Administrator"

### department
- **Type**: String
- **Required**: No
- **Max Length**: 100 characters
- **Example**: "Information Technology"

### isActive
- **Type**: Boolean
- **Required**: No
- **Default**: true
- **Valid Values**: true, false, 1, 0, yes, no (case insensitive)
- **Example**: true

## CSV Format Example

```csv
username,email,first_name,last_name,role,department,is_active
jdoe,john.doe@company.com,John,Doe,Administrator,IT,true
jsmith,jane.smith@company.com,Jane,Smith,Operator,Operations,true
```

## Validation Rules

### Business Rules
1. Usernames must be unique across the system
2. Email addresses must be unique
3. Active users should have complete profile information

### Security Considerations
- Passwords are not included in import/export for security
- User permissions are managed separately
- Email addresses are used for system notifications

## Common Import Issues

### Validation Errors
- **Duplicate username**: Each user must have unique username
- **Duplicate email**: Each user must have unique email address
- **Invalid email format**: Must include @ and valid domain
- **Missing required fields**: Username and email are mandatory

## Export Notes

### Available Fields
- id, username, email, firstName, lastName, role, department, isActive

### Security Restrictions
- Password fields are never exported
- Sensitive information is excluded from exports
- Some organizations may restrict user data exports