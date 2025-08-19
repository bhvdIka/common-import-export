package com.importexport.dto;

import java.time.LocalDateTime;

public class FileMetadata {

    private String fileName;
    private String fileType;
    private long fileSize;
    private String contentType;
    private LocalDateTime uploadedAt;
    private String uploadedBy;
    private String moduleType;

    // Constructors
    public FileMetadata() {
        this.uploadedAt = LocalDateTime.now();
    }

    public FileMetadata(String fileName, String fileType, long fileSize, String contentType, String uploadedBy, String moduleType) {
        this();
        this.fileName = fileName;
        this.fileType = fileType;
        this.fileSize = fileSize;
        this.contentType = contentType;
        this.uploadedBy = uploadedBy;
        this.moduleType = moduleType;
    }

    // Getters and Setters
    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public long getFileSize() {
        return fileSize;
    }

    public void setFileSize(long fileSize) {
        this.fileSize = fileSize;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public LocalDateTime getUploadedAt() {
        return uploadedAt;
    }

    public void setUploadedAt(LocalDateTime uploadedAt) {
        this.uploadedAt = uploadedAt;
    }

    public String getUploadedBy() {
        return uploadedBy;
    }

    public void setUploadedBy(String uploadedBy) {
        this.uploadedBy = uploadedBy;
    }

    public String getModuleType() {
        return moduleType;
    }

    public void setModuleType(String moduleType) {
        this.moduleType = moduleType;
    }
}