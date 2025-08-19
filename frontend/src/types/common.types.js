/**
 * Common API response structure
 */
export const ApiResponseType = {
  success: false, // boolean - request success status
  data: null, // any - response data
  message: '', // string - response message
  errors: [], // array - list of errors if any
  timestamp: null, // Date - response timestamp
  requestId: '' // string - unique request identifier
};

/**
 * User information structure
 */
export const UserType = {
  id: 0, // number - user ID
  username: '', // string - username
  email: '', // string - email address
  firstName: '', // string - first name
  lastName: '', // string - last name
  role: '', // string - user role
  permissions: {}, // object - user permissions by module
  isActive: true // boolean - user active status
};

/**
 * Notification structure
 */
export const NotificationType = {
  id: '', // string - unique notification ID
  message: '', // string - notification message
  type: 'info', // string - info, success, warning, error
  duration: 5000, // number - display duration in milliseconds
  timestamp: null, // Date - notification timestamp
  read: false // boolean - read status
};

/**
 * Module configuration structure
 */
export const ModuleConfigType = {
  name: '', // string - module name
  displayName: '', // string - human readable name
  description: '', // string - module description
  fields: [], // array - available fields
  supportedFormats: [], // array - supported file formats
  permissions: {}, // object - required permissions
  validation: {} // object - validation rules
};

/**
 * File upload progress structure
 */
export const UploadProgressType = {
  percentage: 0, // number - upload percentage (0-100)
  loaded: 0, // number - bytes loaded
  total: 0, // number - total bytes
  status: 'idle', // string - idle, uploading, processing, complete, error
  message: '' // string - status message
};

/**
 * Pagination structure
 */
export const PaginationType = {
  page: 1, // number - current page (1-based)
  pageSize: 20, // number - items per page
  totalItems: 0, // number - total number of items
  totalPages: 0, // number - total number of pages
  hasNext: false, // boolean - has next page
  hasPrevious: false // boolean - has previous page
};