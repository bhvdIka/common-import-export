# Common Import/Export Data Functionality

A comprehensive solution for importing and exporting data across multiple modules (Camera, Robot, Task, User, Map) with support for CSV, Excel, and JSON formats.

## Features

- **Multi-format Support**: Import/Export CSV, Excel (.xlsx/.xls), and JSON files
- **Multi-module**: Support for Camera, Robot, Task, User, and Map modules
- **Data Validation**: Comprehensive validation with detailed error reporting
- **Progress Tracking**: Real-time progress updates for import/export operations
- **Template Generation**: Download template files for easy data preparation
- **Permission-based Access**: Role-based access control for different operations
- **Responsive UI**: Modern React frontend with intuitive interface
- **Error Handling**: Detailed error messages and validation feedback

## Project Structure

```
common-import-export/
├── backend/                    # Spring Boot backend
│   ├── src/main/java/com/importexport/
│   │   ├── config/            # Configuration classes
│   │   ├── controller/        # REST controllers
│   │   ├── service/           # Business logic
│   │   ├── processor/         # File processors
│   │   ├── entity/            # Data entities
│   │   ├── dto/               # Data transfer objects
│   │   ├── util/              # Utility classes
│   │   └── exception/         # Custom exceptions
│   └── src/main/resources/
│       ├── application.yml    # Configuration
│       └── templates/         # Template files
├── frontend/                  # React frontend
│   ├── src/
│   │   ├── components/        # React components
│   │   ├── hooks/             # Custom hooks
│   │   ├── services/          # API services
│   │   ├── utils/             # Utility functions
│   │   ├── types/             # Type definitions
│   │   └── styles/            # CSS styles
│   └── public/               # Static assets
└── docs/                     # Documentation
    ├── api/                  # API documentation
    ├── samples/              # Sample data files
    └── mapping/              # Field mapping docs
```

## Quick Start

### Prerequisites

- Java 17+
- Node.js 16+
- Maven 3.6+

### Backend Setup

1. Navigate to the backend directory:
   ```bash
   cd backend
   ```

2. Install dependencies and run:
   ```bash
   mvn clean install
   mvn spring-boot:run
   ```

3. The backend will start at `http://localhost:8080`

### Frontend Setup

1. Navigate to the frontend directory:
   ```bash
   cd frontend
   ```

2. Install dependencies:
   ```bash
   npm install
   ```

3. Start the development server:
   ```bash
   npm start
   ```

4. The frontend will start at `http://localhost:3000`

## API Endpoints

### Import Data
```
POST /api/{module}/import
```
- Upload and import data from CSV, Excel, or JSON files
- Supports validation-only mode
- Returns detailed import results with error information

### Export Data
```
POST /api/{module}/export
```
- Export data in CSV, Excel, or JSON format
- Support for field selection and filtering
- Customizable sorting options

### Download Template
```
GET /api/{module}/template?format={csv|xlsx}
```
- Download template files for data preparation
- Available for all supported modules

## Supported Modules

### Camera
- **Fields**: name, type, ipAddress, location, isActive, description
- **Use Case**: Security camera management

### Robot
- **Fields**: name, model, serialNumber, manufacturer, isActive, description  
- **Use Case**: Industrial robot inventory

### Task
- **Fields**: name, type, priority, status, assignedTo, dueDate, isActive, description
- **Use Case**: Task and workflow management

### User
- **Fields**: username, email, firstName, lastName, role, department, isActive
- **Use Case**: User account management

### Map
- **Fields**: name, type, resolution, width, height, originX, originY, isActive, description
- **Use Case**: Spatial map management

## File Format Support

### CSV
- UTF-8 encoding
- Comma-separated values
- Header row required
- Quote character: `"`

### Excel
- .xlsx and .xls formats
- First sheet processed
- Header row required
- Automatic data type detection

### JSON
- UTF-8 encoding
- Array of objects or single object
- Nested objects flattened

## Configuration

### File Upload Limits
- **Development**: 5MB
- **Production**: 50MB
- Configurable via `application.yml`

### Environment Configuration

Backend (`application.yml`):
```yaml
app:
  file-upload:
    max-file-size: 10485760
    allowed-file-types: [csv, xlsx, xls, json]
    upload-path: ./uploads
```

Frontend (`.env`):
```
REACT_APP_API_BASE_URL=http://localhost:8080/api
REACT_APP_MAX_FILE_SIZE=10485760
```

## Usage Examples

### Import Camera Data

1. Download the camera template:
   ```bash
   curl -O "http://localhost:8080/api/camera/template?format=csv"
   ```

2. Fill in your camera data and upload via the web interface or API:
   ```bash
   curl -X POST http://localhost:8080/api/camera/import \
     -F "file=@cameras.csv" \
     -F "validateOnly=false"
   ```

### Export User Data

```bash
curl -X POST http://localhost:8080/api/user/export \
  -H "Content-Type: application/json" \
  -d '{
    "fileFormat": "xlsx",
    "fields": ["username", "email", "firstName", "lastName"],
    "filter": "isActive = true",
    "sortBy": "lastName"
  }' \
  --output users.xlsx
```

## Error Handling

The system provides comprehensive error handling with detailed validation messages:

- **Field-level validation**: Email format, required fields, data types
- **Business rule validation**: Unique constraints, referential integrity
- **File format validation**: Supported formats, size limits
- **Row-level reporting**: Specific row and field error identification

## Security

- **Permission-based access**: Role-based controls for import/export operations
- **File validation**: Strict file type and size validation
- **Data sanitization**: Input validation and sanitization
- **Audit logging**: Track import/export operations

## Testing

### Backend Tests
```bash
cd backend
mvn test
```

### Frontend Tests
```bash
cd frontend
npm test
```

## Contributing

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Add tests for new functionality
5. Submit a pull request

## Documentation

- [Import API Documentation](docs/api/import-endpoints.md)
- [Export API Documentation](docs/api/export-endpoints.md)
- [Error Codes](docs/api/error-codes.md)
- [Field Mapping Documentation](docs/mapping/)
- [Sample Data Files](docs/samples/)

## License

This project is licensed under the MIT License.

## Support

For questions or issues, please:
1. Check the documentation in the `docs/` folder
2. Review existing issues
3. Create a new issue with detailed information

---

**Version**: 1.0.0  
**Last Updated**: January 2024