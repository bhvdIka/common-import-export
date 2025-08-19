import api from './api';
import fileService from './fileService';

const importExportService = {
  async importData(moduleType, formData) {
    try {
      const response = await api.post(`/${moduleType}/import`, formData, {
        headers: {
          'Content-Type': 'multipart/form-data'
        }
      });
      return response.data;
    } catch (error) {
      throw new Error(error.response?.data?.message || 'Import failed');
    }
  },

  async exportData(moduleType, exportRequest) {
    try {
      const response = await api.post(`/${moduleType}/export`, exportRequest, {
        responseType: 'blob'
      });
      
      // Create download link
      const filename = `${moduleType}_export.${exportRequest.fileFormat}`;
      fileService.downloadBlob(response.data, filename);
      
      return response.data;
    } catch (error) {
      throw new Error(error.response?.data?.message || 'Export failed');
    }
  },

  async downloadTemplate(moduleType, format = 'csv') {
    try {
      const response = await api.get(`/${moduleType}/template`, {
        params: { format },
        responseType: 'blob'
      });
      
      // Create download link
      const filename = `${moduleType}_template.${format}`;
      fileService.downloadBlob(response.data, filename);
      
      return response.data;
    } catch (error) {
      throw new Error(error.response?.data?.message || 'Template download failed');
    }
  },

  async validateFile(moduleType, formData) {
    try {
      const response = await api.post(`/${moduleType}/import`, formData, {
        headers: {
          'Content-Type': 'multipart/form-data'
        },
        params: { validateOnly: true }
      });
      return response.data;
    } catch (error) {
      throw new Error(error.response?.data?.message || 'Validation failed');
    }
  }
};

export default importExportService;