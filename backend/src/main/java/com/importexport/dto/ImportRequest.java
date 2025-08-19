package com.importexport.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class ImportRequest {

    @NotBlank(message = "Module type is required")
    private String moduleType;

    @NotNull(message = "File is required")
    private String fileName;

    private String fileType;

    private boolean validateOnly = false;

    private boolean skipErrors = false;

    private int batchSize = 100;

    // Constructors
    public ImportRequest() {}

    public ImportRequest(String moduleType, String fileName, String fileType, boolean validateOnly, boolean skipErrors, int batchSize) {
        this.moduleType = moduleType;
        this.fileName = fileName;
        this.fileType = fileType;
        this.validateOnly = validateOnly;
        this.skipErrors = skipErrors;
        this.batchSize = batchSize;
    }

    // Getters and Setters
    public String getModuleType() {
        return moduleType;
    }

    public void setModuleType(String moduleType) {
        this.moduleType = moduleType;
    }

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

    public boolean isValidateOnly() {
        return validateOnly;
    }

    public void setValidateOnly(boolean validateOnly) {
        this.validateOnly = validateOnly;
    }

    public boolean isSkipErrors() {
        return skipErrors;
    }

    public void setSkipErrors(boolean skipErrors) {
        this.skipErrors = skipErrors;
    }

    public int getBatchSize() {
        return batchSize;
    }

    public void setBatchSize(int batchSize) {
        this.batchSize = batchSize;
    }
}