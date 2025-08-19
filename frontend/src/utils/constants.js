export const MODULES = {
  CAMERA: 'camera',
  ROBOT: 'robot',
  TASK: 'task',
  USER: 'user',
  MAP: 'map'
};

export const FILE_FORMATS = {
  CSV: 'csv',
  EXCEL: 'xlsx',
  JSON: 'json'
};

export const IMPORT_STATUS = {
  SUCCESS: 'SUCCESS',
  ERROR: 'ERROR',
  VALIDATION_ERRORS: 'VALIDATION_ERRORS',
  VALID: 'VALID'
};

export const NOTIFICATION_TYPES = {
  SUCCESS: 'success',
  ERROR: 'error',
  WARNING: 'warning',
  INFO: 'info'
};

export const FILE_SIZE_LIMITS = {
  MAX_SIZE: 10 * 1024 * 1024, // 10MB
  MAX_SIZE_DEV: 5 * 1024 * 1024 // 5MB for development
};

export const SUPPORTED_FILE_TYPES = ['.csv', '.xlsx', '.xls', '.json'];

export const API_ENDPOINTS = {
  IMPORT: (module) => `/${module}/import`,
  EXPORT: (module) => `/${module}/export`,
  TEMPLATE: (module) => `/${module}/template`
};

export const FIELD_MAPPINGS = {
  camera: {
    required: ['name', 'type'],
    optional: ['ipAddress', 'location', 'isActive', 'description'],
    all: ['id', 'name', 'type', 'ipAddress', 'location', 'isActive', 'description']
  },
  robot: {
    required: ['name', 'model'],
    optional: ['serialNumber', 'manufacturer', 'isActive', 'description'],
    all: ['id', 'name', 'model', 'serialNumber', 'manufacturer', 'isActive', 'description']
  },
  task: {
    required: ['name', 'type'],
    optional: ['priority', 'status', 'assignedTo', 'dueDate', 'isActive', 'description'],
    all: ['id', 'name', 'type', 'priority', 'status', 'assignedTo', 'dueDate', 'isActive', 'description']
  },
  user: {
    required: ['username', 'email'],
    optional: ['firstName', 'lastName', 'role', 'department', 'isActive'],
    all: ['id', 'username', 'email', 'firstName', 'lastName', 'role', 'department', 'isActive']
  },
  map: {
    required: ['name', 'type'],
    optional: ['resolution', 'width', 'height', 'originX', 'originY', 'isActive', 'description'],
    all: ['id', 'name', 'type', 'resolution', 'width', 'height', 'originX', 'originY', 'isActive', 'description']
  }
};

export const DEFAULT_PAGINATION = {
  PAGE_SIZE: 20,
  MAX_PAGE_SIZE: 100
};

export const VALIDATION_RULES = {
  email: { required: true, email: true },
  name: { required: true, minLength: 2, maxLength: 100 },
  description: { maxLength: 1000 },
  priority: { integer: true, min: 1, max: 10 },
  resolution: { number: true, positive: true },
  width: { integer: true, positive: true },
  height: { integer: true, positive: true }
};