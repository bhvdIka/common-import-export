package com.importexport.service.impl;

import com.importexport.config.FileUploadConfig;
import com.importexport.dto.ExportRequest;
import com.importexport.dto.ImportRequest;
import com.importexport.dto.ImportResponse;
import com.importexport.entity.*;
import com.importexport.processor.FileProcessor;
import com.importexport.repository.*;
import com.importexport.service.ImportExportService;
import com.importexport.service.ValidationService;
import com.importexport.service.AuditService;
import com.importexport.service.DataTransformationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

@Service
public class ImportExportServiceImpl implements ImportExportService {

    @Autowired
    private FileUploadConfig fileUploadConfig;

    @Autowired
    private ValidationService validationService;

    @Autowired
    private AuditService auditService;

    @Autowired
    private DataTransformationService dataTransformationService;

    @Autowired
    private List<FileProcessor> fileProcessors;

    // Repository dependencies
    @Autowired
    private CameraRepository cameraRepository;

    @Autowired
    private RobotRepository robotRepository;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MapRepository mapRepository;

    private final ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(4);

    @Override
    @Transactional
    public ImportResponse importData(MultipartFile file, ImportRequest request) {
        long startTime = System.currentTimeMillis();
        AuditLog auditLog = auditService.createAuditLog("IMPORT", request.getModuleType(), 
                                                        "system", file.getOriginalFilename());
        
        try {
            // Validate file
            if (!validateFile(file)) {
                String errorMsg = "Invalid file format or size";
                auditService.updateAuditLog(auditLog, 0, 0, 0, 
                                          System.currentTimeMillis() - startTime, "FAILURE", errorMsg);
                return new ImportResponse("ERROR", errorMsg, 0, 0, 0);
            }

            // Find appropriate processor
            String fileExtension = getFileExtension(file.getOriginalFilename());
            FileProcessor processor = findProcessor(fileExtension);
            
            if (processor == null) {
                String errorMsg = "Unsupported file format: " + fileExtension;
                auditService.updateAuditLog(auditLog, 0, 0, 0, 
                                          System.currentTimeMillis() - startTime, "FAILURE", errorMsg);
                return new ImportResponse("ERROR", errorMsg, 0, 0, 0);
            }

            // Process file
            List<Map<String, Object>> data = processor.processFile(file.getInputStream());
            
            // Sanitize data
            sanitizeData(data);
            
            // Validate data if not validation-only mode
            ImportResponse response;
            if (request.isValidateOnly()) {
                response = validationService.validateData(data, request.getModuleType());
                auditService.logValidationOperation(request.getModuleType(), "system", 
                                                   file.getOriginalFilename(), response.getTotalRecords(), 
                                                   response.getSuccessfulRecords(), response.getFailedRecords(), 
                                                   System.currentTimeMillis() - startTime, response.getStatus());
            } else {
                // Validate first
                ImportResponse validationResponse = validationService.validateData(data, request.getModuleType());
                
                if (!validationResponse.getErrors().isEmpty() && !request.isSkipErrors()) {
                    auditService.updateAuditLog(auditLog, validationResponse.getTotalRecords(), 
                                              0, validationResponse.getFailedRecords(), 
                                              System.currentTimeMillis() - startTime, "VALIDATION_FAILED", 
                                              "Data validation failed");
                    return validationResponse;
                }
                
                // Process and persist data
                response = processImportData(data, request, validationResponse);
                auditService.logImportOperation(request.getModuleType(), "system", 
                                              file.getOriginalFilename(), response.getTotalRecords(), 
                                              response.getSuccessfulRecords(), response.getFailedRecords(), 
                                              System.currentTimeMillis() - startTime, response.getStatus(), null);
            }
            
            response.setProcessingTimeMs(System.currentTimeMillis() - startTime);
            return response;
            
        } catch (Exception e) {
            String errorMsg = "Import failed: " + e.getMessage();
            auditService.updateAuditLog(auditLog, 0, 0, 0, 
                                      System.currentTimeMillis() - startTime, "FAILURE", errorMsg);
            ImportResponse response = new ImportResponse("ERROR", errorMsg, 0, 0, 0);
            response.setProcessingTimeMs(System.currentTimeMillis() - startTime);
            return response;
        }
    }

    @Override
    public InputStream exportData(ExportRequest request) {
        // TODO: Implement export functionality
        // This would query data based on the request parameters and generate the output file
        String sampleData = "id,name,type\n1,Sample Camera,IP Camera\n2,Sample Robot,AGV";
        return new ByteArrayInputStream(sampleData.getBytes());
    }

    @Override
    public InputStream getTemplate(String moduleType, String fileFormat) {
        // TODO: Load template from resources based on module type and format
        String templateData = "name,type,description\n";
        return new ByteArrayInputStream(templateData.getBytes());
    }

    @Override
    public boolean validateFile(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            return false;
        }

        // Check file size
        if (file.getSize() > fileUploadConfig.getMaxFileSize()) {
            return false;
        }

        // Check file type
        String fileExtension = getFileExtension(file.getOriginalFilename());
        return Arrays.asList(fileUploadConfig.getAllowedFileTypes()).contains(fileExtension);
    }

    private FileProcessor findProcessor(String fileExtension) {
        return fileProcessors.stream()
                .filter(processor -> processor.supports(fileExtension))
                .findFirst()
                .orElse(null);
    }

    private String getFileExtension(String filename) {
        if (filename == null || !filename.contains(".")) {
            return "";
        }
        return filename.substring(filename.lastIndexOf(".") + 1).toLowerCase();
    }

    @Transactional
    private ImportResponse processImportData(List<Map<String, Object>> data, ImportRequest request, ImportResponse validationResponse) {
        int totalRecords = data.size();
        int successfulRecords = 0;
        int failedRecords = 0;
        List<String> errors = new ArrayList<>();
        
        try {
            // Process in batches for better performance
            int batchSize = request.getBatchSize() > 0 ? request.getBatchSize() : 100;
            
            for (int i = 0; i < data.size(); i += batchSize) {
                int endIndex = Math.min(i + batchSize, data.size());
                List<Map<String, Object>> batch = data.subList(i, endIndex);
                
                List<Object> batchEntities = new ArrayList<>();
                
                for (Map<String, Object> row : batch) {
                    try {
                        // Skip invalid records if skipErrors is enabled
                        if (request.isSkipErrors() && hasValidationErrors(row, validationResponse)) {
                            failedRecords++;
                            continue;
                        }
                        
                        Object entity = transformRowToEntity(row, request.getModuleType());
                        if (entity != null) {
                            batchEntities.add(entity);
                        }
                    } catch (Exception e) {
                        failedRecords++;
                        errors.add("Row " + (i + 1) + ": " + e.getMessage());
                        if (!request.isSkipErrors()) {
                            break;
                        }
                    }
                }
                
                // Persist batch
                successfulRecords += persistBatch(batchEntities, request.getModuleType());
            }
            
            String status = failedRecords > 0 ? (successfulRecords > 0 ? "PARTIAL" : "FAILURE") : "SUCCESS";
            String message = failedRecords > 0 ? 
                           String.format("Import completed with %d successful and %d failed records", successfulRecords, failedRecords) :
                           "All records imported successfully";
                           
            ImportResponse response = new ImportResponse(status, message, totalRecords, successfulRecords, failedRecords);
            return response;
            
        } catch (Exception e) {
            return new ImportResponse("FAILURE", "Import failed: " + e.getMessage(), totalRecords, successfulRecords, failedRecords);
        }
    }
    
    private Object transformRowToEntity(Map<String, Object> row, String moduleType) {
        switch (moduleType.toLowerCase()) {
            case "camera":
                return dataTransformationService.transformToCamera(row);
            case "robot":
                return dataTransformationService.transformToRobot(row);
            case "task":
                return dataTransformationService.transformToTask(row);
            case "user":
                return dataTransformationService.transformToUser(row);
            case "map":
                return dataTransformationService.transformToMap(row);
            default:
                throw new IllegalArgumentException("Unsupported module type: " + moduleType);
        }
    }
    
    @SuppressWarnings("unchecked")
    private int persistBatch(List<Object> entities, String moduleType) {
        if (entities.isEmpty()) {
            return 0;
        }
        
        switch (moduleType.toLowerCase()) {
            case "camera":
                List<Camera> cameras = new ArrayList<>();
                for (Object entity : entities) {
                    if (entity instanceof Camera) {
                        cameras.add((Camera) entity);
                    }
                }
                return cameraRepository.saveAll(cameras).size();
            case "robot":
                List<Robot> robots = new ArrayList<>();
                for (Object entity : entities) {
                    if (entity instanceof Robot) {
                        robots.add((Robot) entity);
                    }
                }
                return robotRepository.saveAll(robots).size();
            case "task":
                List<Task> tasks = new ArrayList<>();
                for (Object entity : entities) {
                    if (entity instanceof Task) {
                        tasks.add((Task) entity);
                    }
                }
                return taskRepository.saveAll(tasks).size();
            case "user":
                List<User> users = new ArrayList<>();
                for (Object entity : entities) {
                    if (entity instanceof User) {
                        users.add((User) entity);
                    }
                }
                return userRepository.saveAll(users).size();
            case "map":
                List<com.importexport.entity.Map> maps = new ArrayList<>();
                for (Object entity : entities) {
                    if (entity instanceof com.importexport.entity.Map) {
                        maps.add((com.importexport.entity.Map) entity);
                    }
                }
                return mapRepository.saveAll(maps).size();
            default:
                return 0;
        }
    }
    
    private boolean hasValidationErrors(Map<String, Object> row, ImportResponse validationResponse) {
        // This is a simplified check - in practice, you'd match by row number
        return validationResponse.getErrors() != null && !validationResponse.getErrors().isEmpty();
    }
    
    private void sanitizeData(List<Map<String, Object>> data) {
        for (Map<String, Object> row : data) {
            for (Map.Entry<String, Object> entry : row.entrySet()) {
                if (entry.getValue() instanceof String) {
                    String sanitized = dataTransformationService.sanitizeString((String) entry.getValue());
                    entry.setValue(sanitized);
                }
            }
        }
    }
}