const permissionService = {
  // Mock user permissions - in real implementation, this would fetch from API
  getCurrentUser() {
    return {
      id: 1,
      username: 'demo_user',
      role: 'admin',
      permissions: {
        camera: ['import', 'export', 'template'],
        robot: ['import', 'export', 'template'],
        task: ['import', 'export', 'template'],
        user: ['import', 'template'], // No export permission for users
        map: ['import', 'export', 'template']
      }
    };
  },

  hasPermission(moduleType, action) {
    const user = this.getCurrentUser();
    return user.permissions[moduleType]?.includes(action) || false;
  },

  canImport(moduleType) {
    return this.hasPermission(moduleType, 'import');
  },

  canExport(moduleType) {
    return this.hasPermission(moduleType, 'export');
  },

  canDownloadTemplate(moduleType) {
    return this.hasPermission(moduleType, 'template');
  },

  getModulePermissions(moduleType) {
    const user = this.getCurrentUser();
    return user.permissions[moduleType] || [];
  },

  getAllPermissions() {
    const user = this.getCurrentUser();
    return user.permissions;
  }
};

export default permissionService;