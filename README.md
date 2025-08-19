# Common Import/Export Data Functionality

A comprehensive solution for importing and exporting data across multiple modules (Camera, Robot, Task, User, Map) with support for CSV, Excel, and JSON formats.

**Status: ✅ Phase 1 Complete - Core Infrastructure and Project Structure Implemented**

## Features

- **Multi-format Support**: Import/Export CSV, Excel (.xlsx/.xls), and JSON files
- **Multi-module**: Support for Camera, Robot, Task, User, and Map modules
- **Data Validation**: Comprehensive validation with detailed error reporting
- **Progress Tracking**: Real-time progress updates for import/export operations
- **Template Generation**: Download template files for easy data preparation
- **Permission-based Access**: Role-based access control for different operations
- **Responsive UI**: Modern React frontend with intuitive interface
- **Error Handling**: Detailed error messages and validation feedback

## Quick Start

### Prerequisites
- Java 17+
- Node.js 16+
- Maven 3.6+

### Backend Setup
```bash
cd backend
mvn clean install
mvn spring-boot:run
```
Backend will start at `http://localhost:8080`

### Frontend Setup
```bash
cd frontend
npm install
npm start
```
Frontend will start at `http://localhost:3000`

## API Endpoints

### Import Data
```
POST /api/{module}/import
```

### Export Data
```
POST /api/{module}/export
```

### Download Template
```
GET /api/{module}/template?format={csv|xlsx}
```

## Supported Modules

- **Camera**: Security camera management
- **Robot**: Industrial robot inventory
- **Task**: Task and workflow management
- **User**: User account management
- **Map**: Spatial map management

## Documentation

Complete documentation available in the `docs/` folder:

- [API Documentation](docs/api/)
- [Sample Data Files](docs/samples/)
- [Field Mapping Documentation](docs/mapping/)
- [Comprehensive README](docs/README.md)

## Project Status

✅ **Backend**: Spring Boot application with complete REST API  
✅ **Frontend**: React application with full UI components  
✅ **Documentation**: Comprehensive API and usage documentation  
✅ **Templates**: CSV templates for all modules  
✅ **Validation**: Complete data validation with error reporting  
✅ **Testing**: Backend tests passing, frontend build successful  

---

For detailed setup instructions and usage examples, see [docs/README.md](docs/README.md)
