package com.importexport.service;

import com.importexport.entity.AuditLog;
import com.importexport.repository.AuditLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuditService {

    @Autowired
    private AuditLogRepository auditLogRepository;

    public AuditLog createAuditLog(String operationType, String moduleType, String userId, String fileName) {
        AuditLog auditLog = new AuditLog(operationType, moduleType, userId, fileName);
        return auditLogRepository.save(auditLog);
    }

    public void updateAuditLog(AuditLog auditLog, int totalRecords, int successfulRecords, int failedRecords, 
                               long processingTimeMs, String status, String errorMessage) {
        auditLog.setRecordsProcessed(totalRecords);
        auditLog.setRecordsSuccessful(successfulRecords);
        auditLog.setRecordsFailed(failedRecords);
        auditLog.setProcessingTimeMs(processingTimeMs);
        auditLog.setStatus(status);
        auditLog.setErrorMessage(errorMessage);
        auditLogRepository.save(auditLog);
    }

    public void logImportOperation(String moduleType, String userId, String fileName, 
                                   int totalRecords, int successfulRecords, int failedRecords, 
                                   long processingTimeMs, String status, String errorMessage) {
        AuditLog auditLog = createAuditLog("IMPORT", moduleType, userId, fileName);
        updateAuditLog(auditLog, totalRecords, successfulRecords, failedRecords, 
                       processingTimeMs, status, errorMessage);
    }

    public void logExportOperation(String moduleType, String userId, String fileName, 
                                   int recordsExported, long processingTimeMs, String status, String errorMessage) {
        AuditLog auditLog = createAuditLog("EXPORT", moduleType, userId, fileName);
        updateAuditLog(auditLog, recordsExported, recordsExported, 0, 
                       processingTimeMs, status, errorMessage);
    }

    public void logValidationOperation(String moduleType, String userId, String fileName, 
                                       int totalRecords, int validRecords, int invalidRecords, 
                                       long processingTimeMs, String status) {
        AuditLog auditLog = createAuditLog("VALIDATION", moduleType, userId, fileName);
        updateAuditLog(auditLog, totalRecords, validRecords, invalidRecords, 
                       processingTimeMs, status, null);
    }
}