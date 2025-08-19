import { useState } from 'react';

const useFileUpload = () => {
  const [uploadProgress, setUploadProgress] = useState(0);
  const [isUploading, setIsUploading] = useState(false);
  const [error, setError] = useState(null);

  const uploadFile = async (file, onProgress) => {
    setIsUploading(true);
    setError(null);
    setUploadProgress(0);

    try {
      // Simulate upload progress
      const progressInterval = setInterval(() => {
        setUploadProgress(prev => {
          const newProgress = prev + 10;
          if (newProgress >= 100) {
            clearInterval(progressInterval);
            return 100;
          }
          return newProgress;
        });
      }, 200);

      // This would be replaced with actual upload logic
      await new Promise(resolve => setTimeout(resolve, 2000));

      if (onProgress) {
        onProgress(100);
      }

      return file;
    } catch (err) {
      setError(err.message);
      throw err;
    } finally {
      setIsUploading(false);
    }
  };

  const resetUpload = () => {
    setUploadProgress(0);
    setIsUploading(false);
    setError(null);
  };

  return {
    uploadFile,
    uploadProgress,
    isUploading,
    error,
    resetUpload
  };
};

export default useFileUpload;