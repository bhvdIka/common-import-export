const fileService = {
  downloadBlob(blob, filename) {
    const url = window.URL.createObjectURL(blob);
    const link = document.createElement('a');
    link.href = url;
    link.download = filename;
    document.body.appendChild(link);
    link.click();
    document.body.removeChild(link);
    window.URL.revokeObjectURL(url);
  },

  validateFile(file, allowedTypes, maxSize) {
    const errors = [];

    if (!file) {
      errors.push('No file selected');
      return { isValid: false, errors };
    }

    // Check file size
    if (maxSize && file.size > maxSize) {
      errors.push(`File size exceeds ${Math.round(maxSize / 1024 / 1024)}MB limit`);
    }

    // Check file type
    if (allowedTypes && allowedTypes.length > 0) {
      const fileExtension = '.' + file.name.split('.').pop().toLowerCase();
      if (!allowedTypes.includes(fileExtension)) {
        errors.push(`Unsupported file format. Allowed: ${allowedTypes.join(', ')}`);
      }
    }

    return {
      isValid: errors.length === 0,
      errors
    };
  },

  getFileExtension(filename) {
    if (!filename || !filename.includes('.')) {
      return '';
    }
    return filename.split('.').pop().toLowerCase();
  },

  formatFileSize(bytes) {
    if (bytes === 0) return '0 Bytes';
    const k = 1024;
    const sizes = ['Bytes', 'KB', 'MB', 'GB'];
    const i = Math.floor(Math.log(bytes) / Math.log(k));
    return parseFloat((bytes / Math.pow(k, i)).toFixed(2)) + ' ' + sizes[i];
  },

  async readFileAsText(file) {
    return new Promise((resolve, reject) => {
      const reader = new FileReader();
      reader.onload = () => resolve(reader.result);
      reader.onerror = reject;
      reader.readAsText(file);
    });
  },

  async readFileAsArrayBuffer(file) {
    return new Promise((resolve, reject) => {
      const reader = new FileReader();
      reader.onload = () => resolve(reader.result);
      reader.onerror = reject;
      reader.readAsArrayBuffer(file);
    });
  }
};

export default fileService;