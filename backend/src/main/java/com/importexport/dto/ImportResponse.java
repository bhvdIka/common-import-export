package com.importexport.dto;

import java.time.LocalDateTime;
import java.util.List;

public class ImportResponse {

    private String status;
    private String message;
    private int totalRecords;
    private int successfulRecords;
    private int failedRecords;
    private List<ValidationError> errors;
    private LocalDateTime processedAt;
    private long processingTimeMs;

    // Constructors
    public ImportResponse() {
        this.processedAt = LocalDateTime.now();
    }

    public ImportResponse(String status, String message, int totalRecords, int successfulRecords, int failedRecords) {
        this();
        this.status = status;
        this.message = message;
        this.totalRecords = totalRecords;
        this.successfulRecords = successfulRecords;
        this.failedRecords = failedRecords;
    }

    // Getters and Setters
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getTotalRecords() {
        return totalRecords;
    }

    public void setTotalRecords(int totalRecords) {
        this.totalRecords = totalRecords;
    }

    public int getSuccessfulRecords() {
        return successfulRecords;
    }

    public void setSuccessfulRecords(int successfulRecords) {
        this.successfulRecords = successfulRecords;
    }

    public int getFailedRecords() {
        return failedRecords;
    }

    public void setFailedRecords(int failedRecords) {
        this.failedRecords = failedRecords;
    }

    public List<ValidationError> getErrors() {
        return errors;
    }

    public void setErrors(List<ValidationError> errors) {
        this.errors = errors;
    }

    public LocalDateTime getProcessedAt() {
        return processedAt;
    }

    public void setProcessedAt(LocalDateTime processedAt) {
        this.processedAt = processedAt;
    }

    public long getProcessingTimeMs() {
        return processingTimeMs;
    }

    public void setProcessingTimeMs(long processingTimeMs) {
        this.processingTimeMs = processingTimeMs;
    }
}