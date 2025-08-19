import React, { useState, useRef } from 'react';
import Button from '../common/Button';

const FileUpload = ({ onFileSelect, acceptedFormats, maxSize }) => {
  const [dragOver, setDragOver] = useState(false);
  const [selectedFile, setSelectedFile] = useState(null);
  const fileInputRef = useRef(null);

  const validateFile = (file) => {
    if (!file) return false;
    
    // Check file size
    if (file.size > maxSize) {
      throw new Error(`File size exceeds ${Math.round(maxSize / 1024 / 1024)}MB limit`);
    }
    
    // Check file format
    const fileExtension = '.' + file.name.split('.').pop().toLowerCase();
    if (!acceptedFormats.includes(fileExtension)) {
      throw new Error(`Unsupported file format. Accepted formats: ${acceptedFormats.join(', ')}`);
    }
    
    return true;
  };

  const handleFileChange = (event) => {
    const file = event.target.files[0];
    handleFile(file);
  };

  const handleFile = (file) => {
    try {
      if (validateFile(file)) {
        setSelectedFile(file);
      }
    } catch (error) {
      alert(error.message);
    }
  };

  const handleDragOver = (event) => {
    event.preventDefault();
    setDragOver(true);
  };

  const handleDragLeave = (event) => {
    event.preventDefault();
    setDragOver(false);
  };

  const handleDrop = (event) => {
    event.preventDefault();
    setDragOver(false);
    
    const files = event.dataTransfer.files;
    if (files.length > 0) {
      handleFile(files[0]);
    }
  };

  const handleUpload = () => {
    if (selectedFile && onFileSelect) {
      onFileSelect(selectedFile);
    }
  };

  const openFileDialog = () => {
    fileInputRef.current?.click();
  };

  return (
    <div className="file-upload-container">
      <div 
        className={`file-drop-zone ${dragOver ? 'drag-over' : ''} ${selectedFile ? 'has-file' : ''}`}
        onDragOver={handleDragOver}
        onDragLeave={handleDragLeave}
        onDrop={handleDrop}
        onClick={openFileDialog}
      >
        <input
          ref={fileInputRef}
          type="file"
          onChange={handleFileChange}
          accept={acceptedFormats.join(',')}
          style={{ display: 'none' }}
        />
        
        {selectedFile ? (
          <div className="selected-file">
            <div className="file-icon">📄</div>
            <div className="file-info">
              <div className="file-name">{selectedFile.name}</div>
              <div className="file-size">
                {Math.round(selectedFile.size / 1024)} KB
              </div>
            </div>
          </div>
        ) : (
          <div className="upload-prompt">
            <div className="upload-icon">⬆️</div>
            <div className="upload-text">
              <strong>Click to select</strong> or drag and drop your file here
            </div>
            <div className="upload-hint">
              Supported formats: {acceptedFormats.join(', ')}
            </div>
            <div className="upload-hint">
              Maximum size: {Math.round(maxSize / 1024 / 1024)}MB
            </div>
          </div>
        )}
      </div>
      
      {selectedFile && (
        <div className="file-actions">
          <Button 
            onClick={handleUpload}
            variant="primary"
            className="upload-btn"
          >
            Upload and Process
          </Button>
          <Button 
            onClick={() => setSelectedFile(null)}
            variant="outline"
            className="clear-btn"
          >
            Clear
          </Button>
        </div>
      )}
    </div>
  );
};

export default FileUpload;