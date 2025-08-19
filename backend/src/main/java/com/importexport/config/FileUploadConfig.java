package com.importexport.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "app.file-upload")
public class FileUploadConfig {

    private long maxFileSize = 10485760; // 10MB
    private long maxRequestSize = 10485760; // 10MB
    private String[] allowedFileTypes = {"csv", "xlsx", "xls", "json"};
    private String uploadPath = "./uploads";

    public long getMaxFileSize() {
        return maxFileSize;
    }

    public void setMaxFileSize(long maxFileSize) {
        this.maxFileSize = maxFileSize;
    }

    public long getMaxRequestSize() {
        return maxRequestSize;
    }

    public void setMaxRequestSize(long maxRequestSize) {
        this.maxRequestSize = maxRequestSize;
    }

    public String[] getAllowedFileTypes() {
        return allowedFileTypes;
    }

    public void setAllowedFileTypes(String[] allowedFileTypes) {
        this.allowedFileTypes = allowedFileTypes;
    }

    public String getUploadPath() {
        return uploadPath;
    }

    public void setUploadPath(String uploadPath) {
        this.uploadPath = uploadPath;
    }
}