package com.importexport.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "audit_logs")
public class AuditLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "operation_type", nullable = false)
    private String operationType; // IMPORT, EXPORT, VALIDATION

    @Column(name = "module_type", nullable = false)
    private String moduleType; // CAMERA, ROBOT, TASK, USER, MAP

    @Column(name = "user_id")
    private String userId;

    @Column(name = "file_name")
    private String fileName;

    @Column(name = "records_processed")
    private Integer recordsProcessed;

    @Column(name = "records_successful")
    private Integer recordsSuccessful;

    @Column(name = "records_failed")
    private Integer recordsFailed;

    @Column(name = "processing_time_ms")
    private Long processingTimeMs;

    @Column(name = "status", nullable = false)
    private String status; // SUCCESS, FAILURE, PARTIAL

    @Column(name = "error_message", length = 2000)
    private String errorMessage;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    // Constructors
    public AuditLog() {
        this.createdAt = LocalDateTime.now();
    }

    public AuditLog(String operationType, String moduleType, String userId, String fileName) {
        this();
        this.operationType = operationType;
        this.moduleType = moduleType;
        this.userId = userId;
        this.fileName = fileName;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOperationType() {
        return operationType;
    }

    public void setOperationType(String operationType) {
        this.operationType = operationType;
    }

    public String getModuleType() {
        return moduleType;
    }

    public void setModuleType(String moduleType) {
        this.moduleType = moduleType;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public Integer getRecordsProcessed() {
        return recordsProcessed;
    }

    public void setRecordsProcessed(Integer recordsProcessed) {
        this.recordsProcessed = recordsProcessed;
    }

    public Integer getRecordsSuccessful() {
        return recordsSuccessful;
    }

    public void setRecordsSuccessful(Integer recordsSuccessful) {
        this.recordsSuccessful = recordsSuccessful;
    }

    public Integer getRecordsFailed() {
        return recordsFailed;
    }

    public void setRecordsFailed(Integer recordsFailed) {
        this.recordsFailed = recordsFailed;
    }

    public Long getProcessingTimeMs() {
        return processingTimeMs;
    }

    public void setProcessingTimeMs(Long processingTimeMs) {
        this.processingTimeMs = processingTimeMs;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}