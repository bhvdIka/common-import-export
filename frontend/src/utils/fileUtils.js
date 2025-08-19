export const fileUtils = {
  validateFileType(filename, allowedTypes) {
    if (!filename || !allowedTypes.length) return false;
    const extension = filename.split('.').pop().toLowerCase();
    return allowedTypes.some(type => type.toLowerCase().includes(extension));
  },

  validateFileSize(file, maxSizeBytes) {
    if (!file || !maxSizeBytes) return false;
    return file.size <= maxSizeBytes;
  },

  getFileExtension(filename) {
    if (!filename) return '';
    return filename.split('.').pop().toLowerCase();
  },

  formatBytes(bytes, decimals = 2) {
    if (bytes === 0) return '0 Bytes';
    const k = 1024;
    const dm = decimals < 0 ? 0 : decimals;
    const sizes = ['Bytes', 'KB', 'MB', 'GB', 'TB'];
    const i = Math.floor(Math.log(bytes) / Math.log(k));
    return parseFloat((bytes / Math.pow(k, i)).toFixed(dm)) + ' ' + sizes[i];
  },

  generateFileName(prefix, extension, timestamp = true) {
    const base = prefix || 'file';
    const ext = extension.startsWith('.') ? extension : '.' + extension;
    const time = timestamp ? '_' + new Date().toISOString().replace(/[:.]/g, '-') : '';
    return base + time + ext;
  },

  isImageFile(filename) {
    const imageExtensions = ['jpg', 'jpeg', 'png', 'gif', 'bmp', 'svg', 'webp'];
    const extension = this.getFileExtension(filename);
    return imageExtensions.includes(extension);
  },

  isDocumentFile(filename) {
    const documentExtensions = ['pdf', 'doc', 'docx', 'xls', 'xlsx', 'csv', 'txt', 'json'];
    const extension = this.getFileExtension(filename);
    return documentExtensions.includes(extension);
  }
};