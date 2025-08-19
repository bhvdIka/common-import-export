import React from 'react';
import Button from '../common/Button';

const ImportResult = ({ result, onStartOver, onClose }) => {
  if (!result) return null;

  const getStatusColor = (status) => {
    switch (status) {
      case 'SUCCESS':
        return 'green';
      case 'VALID':
        return 'blue';
      case 'VALIDATION_ERRORS':
        return 'orange';
      case 'ERROR':
        return 'red';
      default:
        return 'gray';
    }
  };

  const getStatusIcon = (status) => {
    switch (status) {
      case 'SUCCESS':
        return '✅';
      case 'VALID':
        return '✅';
      case 'VALIDATION_ERRORS':
        return '⚠️';
      case 'ERROR':
        return '❌';
      default:
        return 'ℹ️';
    }
  };

  return (
    <div className="import-result">
      <div className="result-header">
        <div className={`status-indicator ${getStatusColor(result.status)}`}>
          <span className="status-icon">{getStatusIcon(result.status)}</span>
          <span className="status-text">{result.status}</span>
        </div>
        <h3>{result.message}</h3>
      </div>

      <div className="result-summary">
        <div className="summary-grid">
          <div className="summary-item">
            <div className="summary-label">Total Records</div>
            <div className="summary-value">{result.totalRecords}</div>
          </div>
          <div className="summary-item">
            <div className="summary-label">Successful</div>
            <div className="summary-value success">{result.successfulRecords}</div>
          </div>
          <div className="summary-item">
            <div className="summary-label">Failed</div>
            <div className="summary-value error">{result.failedRecords}</div>
          </div>
          {result.processingTimeMs && (
            <div className="summary-item">
              <div className="summary-label">Processing Time</div>
              <div className="summary-value">{result.processingTimeMs}ms</div>
            </div>
          )}
        </div>
      </div>

      {result.errors && result.errors.length > 0 && (
        <div className="result-errors">
          <h4>Validation Errors ({result.errors.length})</h4>
          <div className="errors-container">
            {result.errors.slice(0, 10).map((error, index) => (
              <div key={index} className="error-item">
                <div className="error-location">
                  Row {error.row}, Field: {error.field}
                </div>
                <div className="error-message">{error.errorMessage}</div>
                {error.actualValue && (
                  <div className="error-details">
                    <span className="error-actual">Actual: "{error.actualValue}"</span>
                    {error.expectedValue && (
                      <span className="error-expected">Expected: {error.expectedValue}</span>
                    )}
                  </div>
                )}
              </div>
            ))}
            {result.errors.length > 10 && (
              <div className="error-more">
                ... and {result.errors.length - 10} more errors
              </div>
            )}
          </div>
        </div>
      )}

      <div className="result-actions">
        <Button 
          onClick={onStartOver}
          variant="primary"
          className="start-over-btn"
        >
          Import Another File
        </Button>
        <Button 
          onClick={onClose}
          variant="outline"
          className="close-btn"
        >
          Close
        </Button>
      </div>
    </div>
  );
};

export default ImportResult;