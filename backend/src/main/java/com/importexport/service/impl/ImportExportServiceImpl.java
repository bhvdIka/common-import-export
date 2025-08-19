package com.importexport.service.impl;

import com.importexport.config.FileUploadConfig;
import com.importexport.dto.ExportRequest;
import com.importexport.dto.ImportRequest;
import com.importexport.dto.ImportResponse;
import com.importexport.processor.FileProcessor;
import com.importexport.service.ImportExportService;
import com.importexport.service.ValidationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Service
public class ImportExportServiceImpl implements ImportExportService {

    @Autowired
    private FileUploadConfig fileUploadConfig;

    @Autowired
    private ValidationService validationService;

    @Autowired
    private List<FileProcessor> fileProcessors;

    @Override
    public ImportResponse importData(MultipartFile file, ImportRequest request) {
        long startTime = System.currentTimeMillis();
        
        try {
            // Validate file
            if (!validateFile(file)) {
                return new ImportResponse("ERROR", "Invalid file format or size", 0, 0, 0);
            }

            // Find appropriate processor
            String fileExtension = getFileExtension(file.getOriginalFilename());
            FileProcessor processor = findProcessor(fileExtension);
            
            if (processor == null) {
                return new ImportResponse("ERROR", "Unsupported file format: " + fileExtension, 0, 0, 0);
            }

            // Process file
            List<Map<String, Object>> data = processor.processFile(file.getInputStream());
            
            // Validate data if not validation-only mode
            ImportResponse response;
            if (request.isValidateOnly()) {
                response = validationService.validateData(data, request.getModuleType());
            } else {
                response = processImportData(data, request);
            }
            
            response.setProcessingTimeMs(System.currentTimeMillis() - startTime);
            return response;
            
        } catch (Exception e) {
            ImportResponse response = new ImportResponse("ERROR", "Import failed: " + e.getMessage(), 0, 0, 0);
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

    private ImportResponse processImportData(List<Map<String, Object>> data, ImportRequest request) {
        // TODO: Implement actual data processing and persistence
        // This would save the data to the database based on the module type
        int totalRecords = data.size();
        return new ImportResponse("SUCCESS", "Data imported successfully", totalRecords, totalRecords, 0);
    }
}