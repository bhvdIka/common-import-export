package com.importexport.dto;

import jakarta.validation.constraints.NotBlank;
import java.util.List;

public class ExportRequest {

    @NotBlank(message = "Module type is required")
    private String moduleType;

    @NotBlank(message = "File format is required")
    private String fileFormat;

    private List<String> fields;

    private String filter;

    private String sortBy;

    private String sortOrder = "ASC";

    private boolean includeInactive = false;

    // Constructors
    public ExportRequest() {}

    public ExportRequest(String moduleType, String fileFormat, List<String> fields, String filter, String sortBy, String sortOrder, boolean includeInactive) {
        this.moduleType = moduleType;
        this.fileFormat = fileFormat;
        this.fields = fields;
        this.filter = filter;
        this.sortBy = sortBy;
        this.sortOrder = sortOrder;
        this.includeInactive = includeInactive;
    }

    // Getters and Setters
    public String getModuleType() {
        return moduleType;
    }

    public void setModuleType(String moduleType) {
        this.moduleType = moduleType;
    }

    public String getFileFormat() {
        return fileFormat;
    }

    public void setFileFormat(String fileFormat) {
        this.fileFormat = fileFormat;
    }

    public List<String> getFields() {
        return fields;
    }

    public void setFields(List<String> fields) {
        this.fields = fields;
    }

    public String getFilter() {
        return filter;
    }

    public void setFilter(String filter) {
        this.filter = filter;
    }

    public String getSortBy() {
        return sortBy;
    }

    public void setSortBy(String sortBy) {
        this.sortBy = sortBy;
    }

    public String getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(String sortOrder) {
        this.sortOrder = sortOrder;
    }

    public boolean isIncludeInactive() {
        return includeInactive;
    }

    public void setIncludeInactive(boolean includeInactive) {
        this.includeInactive = includeInactive;
    }
}