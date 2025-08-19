import { useState } from 'react';

const usePermission = () => {
  // Mock permission system - in real implementation, this would check against user roles/permissions
  const [permissions] = useState({
    camera: { import: true, export: true, template: true },
    robot: { import: true, export: true, template: true },
    task: { import: true, export: true, template: true },
    user: { import: true, export: false, template: true }, // Example: no export for users
    map: { import: true, export: true, template: true }
  });

  const hasPermission = (moduleType, action) => {
    return permissions[moduleType]?.[action] || false;
  };

  const canImport = (moduleType) => hasPermission(moduleType, 'import');
  const canExport = (moduleType) => hasPermission(moduleType, 'export');
  const canDownloadTemplate = (moduleType) => hasPermission(moduleType, 'template');

  return {
    hasPermission,
    canImport,
    canExport,
    canDownloadTemplate,
    permissions
  };
};

export default usePermission;