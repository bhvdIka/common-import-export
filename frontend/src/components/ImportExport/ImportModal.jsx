import React, { useState } from 'react';
import Modal from '../common/Modal';
import FileUpload from './FileUpload';
import ImportResult from './ImportResult';
import ProgressBar from './ProgressBar';
import useImportExport from '../../hooks/useImportExport';
import useFileUpload from '../../hooks/useFileUpload';

const ImportModal = ({ moduleType, onClose }) => {
  const [step, setStep] = useState('upload'); // 'upload', 'processing', 'result'
  const [importResult, setImportResult] = useState(null);
  const [validateOnly, setValidateOnly] = useState(false);
  const [skipErrors, setSkipErrors] = useState(false);
  
  const { importData } = useImportExport(moduleType);
  const { 
    uploadProgress, 
    isUploading, 
    error: uploadError 
  } = useFileUpload();

  const handleFileSelect = async (file) => {
    setStep('processing');
    
    try {
      const formData = new FormData();
      formData.append('file', file);
      formData.append('validateOnly', validateOnly);
      formData.append('skipErrors', skipErrors);
      
      const result = await importData(formData);
      setImportResult(result);
      setStep('result');
    } catch (error) {
      setImportResult({
        status: 'ERROR',
        message: error.message,
        totalRecords: 0,
        successfulRecords: 0,
        failedRecords: 0,
        errors: []
      });
      setStep('result');
    }
  };

  const handleStartOver = () => {
    setStep('upload');
    setImportResult(null);
  };

  const renderContent = () => {
    switch (step) {
      case 'upload':
        return (
          <div className="import-upload-step">
            <h3>Import {moduleType} Data</h3>
            
            <div className="import-options">
              <label className="checkbox-label">
                <input
                  type="checkbox"
                  checked={validateOnly}
                  onChange={(e) => setValidateOnly(e.target.checked)}
                />
                Validate only (don't import)
              </label>
              
              <label className="checkbox-label">
                <input
                  type="checkbox"
                  checked={skipErrors}
                  onChange={(e) => setSkipErrors(e.target.checked)}
                />
                Skip errors and continue
              </label>
            </div>
            
            <FileUpload 
              onFileSelect={handleFileSelect}
              acceptedFormats={['.csv', '.xlsx', '.xls', '.json']}
              maxSize={10 * 1024 * 1024} // 10MB
            />
            
            {uploadError && (
              <div className="error-message">{uploadError}</div>
            )}
          </div>
        );
        
      case 'processing':
        return (
          <div className="import-processing-step">
            <h3>Processing {moduleType} Data...</h3>
            <ProgressBar 
              progress={uploadProgress} 
              status={isUploading ? 'Uploading...' : 'Processing...'}
            />
          </div>
        );
        
      case 'result':
        return (
          <div className="import-result-step">
            <ImportResult 
              result={importResult}
              onStartOver={handleStartOver}
              onClose={onClose}
            />
          </div>
        );
        
      default:
        return null;
    }
  };

  return (
    <Modal
      isOpen={true}
      onClose={onClose}
      title={`Import ${moduleType} Data`}
      size="large"
    >
      {renderContent()}
    </Modal>
  );
};

export default ImportModal;