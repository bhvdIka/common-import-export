import { useState } from 'react';
import importExportService from '../services/importExportService';

const useImportExport = (moduleType) => {
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);

  const importData = async (formData) => {
    setLoading(true);
    setError(null);
    
    try {
      const response = await importExportService.importData(moduleType, formData);
      return response;
    } catch (err) {
      setError(err.message);
      throw err;
    } finally {
      setLoading(false);
    }
  };

  const exportData = async (exportRequest) => {
    setLoading(true);
    setError(null);
    
    try {
      const response = await importExportService.exportData(moduleType, exportRequest);
      return response;
    } catch (err) {
      setError(err.message);
      throw err;
    } finally {
      setLoading(false);
    }
  };

  const downloadTemplate = async (format = 'csv') => {
    setLoading(true);
    setError(null);
    
    try {
      const response = await importExportService.downloadTemplate(moduleType, format);
      return response;
    } catch (err) {
      setError(err.message);
      throw err;
    } finally {
      setLoading(false);
    }
  };

  return {
    importData,
    exportData,
    downloadTemplate,
    loading,
    error
  };
};

export default useImportExport;