import React, { useState } from 'react';
import Button from '../common/Button';

const ExportDropdown = ({ moduleType, onExport, onClose }) => {
  const [exportRequest, setExportRequest] = useState({
    fileFormat: 'csv',
    fields: [],
    filter: '',
    sortBy: '',
    sortOrder: 'ASC',
    includeInactive: false
  });

  const formatOptions = [
    { value: 'csv', label: 'CSV' },
    { value: 'xlsx', label: 'Excel (XLSX)' },
    { value: 'json', label: 'JSON' }
  ];

  const handleFieldChange = (field, checked) => {
    setExportRequest(prev => ({
      ...prev,
      fields: checked 
        ? [...prev.fields, field]
        : prev.fields.filter(f => f !== field)
    }));
  };

  const handleExport = () => {
    onExport(exportRequest);
  };

  const getAvailableFields = () => {
    // This would typically come from configuration or API
    const fieldMap = {
      camera: ['id', 'name', 'type', 'ipAddress', 'location', 'isActive', 'description'],
      robot: ['id', 'name', 'model', 'serialNumber', 'manufacturer', 'isActive', 'description'],
      task: ['id', 'name', 'type', 'priority', 'status', 'assignedTo', 'dueDate', 'isActive', 'description'],
      user: ['id', 'username', 'email', 'firstName', 'lastName', 'role', 'department', 'isActive'],
      map: ['id', 'name', 'type', 'resolution', 'width', 'height', 'originX', 'originY', 'isActive', 'description']
    };
    return fieldMap[moduleType] || [];
  };

  return (
    <div className="export-dropdown">
      <div className="export-overlay" onClick={onClose} />
      <div className="export-panel">
        <div className="export-header">
          <h3>Export {moduleType} Data</h3>
          <button className="close-btn" onClick={onClose}>×</button>
        </div>

        <div className="export-form">
          <div className="form-group">
            <label>File Format</label>
            <select 
              value={exportRequest.fileFormat}
              onChange={(e) => setExportRequest(prev => ({ ...prev, fileFormat: e.target.value }))}
            >
              {formatOptions.map(option => (
                <option key={option.value} value={option.value}>
                  {option.label}
                </option>
              ))}
            </select>
          </div>

          <div className="form-group">
            <label>Fields to Export</label>
            <div className="fields-checkbox-group">
              {getAvailableFields().map(field => (
                <label key={field} className="checkbox-label">
                  <input
                    type="checkbox"
                    checked={exportRequest.fields.includes(field)}
                    onChange={(e) => handleFieldChange(field, e.target.checked)}
                  />
                  {field}
                </label>
              ))}
            </div>
            <button 
              type="button" 
              onClick={() => setExportRequest(prev => ({ ...prev, fields: getAvailableFields() }))}
              className="select-all-btn"
            >
              Select All
            </button>
          </div>

          <div className="form-group">
            <label>Sort By</label>
            <select 
              value={exportRequest.sortBy}
              onChange={(e) => setExportRequest(prev => ({ ...prev, sortBy: e.target.value }))}
            >
              <option value="">No sorting</option>
              {getAvailableFields().map(field => (
                <option key={field} value={field}>{field}</option>
              ))}
            </select>
          </div>

          <div className="form-group">
            <label>Sort Order</label>
            <select 
              value={exportRequest.sortOrder}
              onChange={(e) => setExportRequest(prev => ({ ...prev, sortOrder: e.target.value }))}
            >
              <option value="ASC">Ascending</option>
              <option value="DESC">Descending</option>
            </select>
          </div>

          <div className="form-group">
            <label className="checkbox-label">
              <input
                type="checkbox"
                checked={exportRequest.includeInactive}
                onChange={(e) => setExportRequest(prev => ({ ...prev, includeInactive: e.target.checked }))}
              />
              Include inactive records
            </label>
          </div>
        </div>

        <div className="export-actions">
          <Button onClick={handleExport} variant="primary">
            Export Data
          </Button>
          <Button onClick={onClose} variant="outline">
            Cancel
          </Button>
        </div>
      </div>
    </div>
  );
};

export default ExportDropdown;