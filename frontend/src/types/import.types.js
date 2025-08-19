/**
 * Import request configuration
 */
export const ImportRequestType = {
  moduleType: '', // string - camera, robot, task, user, map
  fileName: '', // string - name of the uploaded file
  fileType: '', // string - csv, xlsx, json
  validateOnly: false, // boolean - if true, only validate don't import
  skipErrors: false, // boolean - if true, skip invalid records and continue
  batchSize: 100 // number - batch size for processing
};

/**
 * Import response structure
 */
export const ImportResponseType = {
  status: '', // string - SUCCESS, ERROR, VALIDATION_ERRORS, VALID
  message: '', // string - human readable message
  totalRecords: 0, // number - total records processed
  successfulRecords: 0, // number - successfully imported records
  failedRecords: 0, // number - failed records
  errors: [], // array of ValidationError objects
  processedAt: null, // Date - when processing completed
  processingTimeMs: 0 // number - processing time in milliseconds
};

/**
 * Validation error structure
 */
export const ValidationErrorType = {
  row: 0, // number - row number where error occurred
  field: '', // string - field name
  errorCode: '', // string - error code (REQUIRED, INVALID_EMAIL, etc.)
  errorMessage: '', // string - human readable error message
  actualValue: '', // string - actual value that caused error
  expectedValue: '' // string - expected value or format
};

/**
 * File metadata structure
 */
export const FileMetadataType = {
  fileName: '', // string - original file name
  fileType: '', // string - file extension
  fileSize: 0, // number - file size in bytes
  contentType: '', // string - MIME content type
  uploadedAt: null, // Date - upload timestamp
  uploadedBy: '', // string - user who uploaded
  moduleType: '' // string - module type
};