import React, { useState } from 'react';
import ImportModal from './ImportModal';
import ExportDropdown from './ExportDropdown';
import Button from '../common/Button';
import useImportExport from '../../hooks/useImportExport';
import useNotification from '../../hooks/useNotification';
import '../../styles/components/ImportExport.css';

const ImportExportButton = ({ moduleType }) => {
  const [showImportModal, setShowImportModal] = useState(false);
  const [showExportDropdown, setShowExportDropdown] = useState(false);
  const { exportData, downloadTemplate } = useImportExport(moduleType);
  const { showNotification } = useNotification();

  const handleImport = () => {
    setShowImportModal(true);
  };

  const handleExport = () => {
    setShowExportDropdown(true);
  };

  const handleDownloadTemplate = async (format = 'csv') => {
    try {
      await downloadTemplate(format);
      showNotification(`Template downloaded successfully`, 'success');
    } catch (error) {
      showNotification(`Failed to download template: ${error.message}`, 'error');
    }
  };

  const handleExportData = async (exportRequest) => {
    try {
      await exportData(exportRequest);
      showNotification('Data exported successfully', 'success');
      setShowExportDropdown(false);
    } catch (error) {
      showNotification(`Export failed: ${error.message}`, 'error');
    }
  };

  return (
    <div className="import-export-container">
      <div className="button-group">
        <Button 
          onClick={handleImport}
          variant="primary"
          className="import-btn"
        >
          Import Data
        </Button>
        
        <Button 
          onClick={handleExport}
          variant="secondary"
          className="export-btn"
        >
          Export Data
        </Button>
        
        <Button 
          onClick={() => handleDownloadTemplate('csv')}
          variant="outline"
          className="template-btn"
        >
          Download Template
        </Button>
      </div>

      {showImportModal && (
        <ImportModal
          moduleType={moduleType}
          onClose={() => setShowImportModal(false)}
        />
      )}

      {showExportDropdown && (
        <ExportDropdown
          moduleType={moduleType}
          onExport={handleExportData}
          onClose={() => setShowExportDropdown(false)}
        />
      )}
    </div>
  );
};

export default ImportExportButton;