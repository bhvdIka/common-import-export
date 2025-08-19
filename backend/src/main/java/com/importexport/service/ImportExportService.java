package com.importexport.service;

import com.importexport.dto.ExportRequest;
import com.importexport.dto.ImportRequest;
import com.importexport.dto.ImportResponse;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;

public interface ImportExportService {

    /**
     * Import data from uploaded file
     * @param file The uploaded file
     * @param request Import request parameters
     * @return Import response with results
     */
    ImportResponse importData(MultipartFile file, ImportRequest request);

    /**
     * Export data to specified format
     * @param request Export request parameters
     * @return InputStream of the exported file
     */
    InputStream exportData(ExportRequest request);

    /**
     * Get template file for the specified module
     * @param moduleType The module type
     * @param fileFormat The file format
     * @return InputStream of the template file
     */
    InputStream getTemplate(String moduleType, String fileFormat);

    /**
     * Validate file format and size
     * @param file The file to validate
     * @return true if valid, false otherwise
     */
    boolean validateFile(MultipartFile file);
}