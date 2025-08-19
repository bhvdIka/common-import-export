/**
 * Export request configuration
 */
export const ExportRequestType = {
  moduleType: '', // string - camera, robot, task, user, map
  fileFormat: 'csv', // string - csv, xlsx, json
  fields: [], // array of strings - fields to include in export
  filter: '', // string - filter criteria (SQL-like syntax)
  sortBy: '', // string - field to sort by
  sortOrder: 'ASC', // string - ASC or DESC
  includeInactive: false // boolean - include inactive records
};

/**
 * Export response structure
 */
export const ExportResponseType = {
  status: '', // string - SUCCESS, ERROR
  message: '', // string - human readable message
  fileName: '', // string - generated file name
  fileSize: 0, // number - file size in bytes
  recordCount: 0, // number - number of exported records
  exportedAt: null, // Date - export timestamp
  downloadUrl: '' // string - URL to download the file (if applicable)
};

/**
 * Export field configuration
 */
export const ExportFieldType = {
  name: '', // string - field name
  label: '', // string - human readable label
  type: '', // string - data type (string, number, boolean, date)
  required: false, // boolean - is field required
  defaultValue: null, // any - default value if not specified
  format: '' // string - format specification for dates/numbers
};

/**
 * Export options for different formats
 */
export const ExportFormatOptions = {
  csv: {
    delimiter: ',', // string - field delimiter
    quote: '"', // string - quote character
    escape: '"', // string - escape character
    header: true, // boolean - include header row
    encoding: 'utf-8' // string - file encoding
  },
  xlsx: {
    sheetName: 'Data', // string - worksheet name
    includeHeader: true, // boolean - include header row
    autoFitColumns: true, // boolean - auto-fit column widths
    freezeHeader: true // boolean - freeze header row
  },
  json: {
    pretty: true, // boolean - pretty print JSON
    arrayFormat: true, // boolean - export as array of objects
    includeMetadata: false // boolean - include export metadata
  }
};