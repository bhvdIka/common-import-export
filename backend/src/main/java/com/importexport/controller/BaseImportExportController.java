package com.importexport.controller;

import com.importexport.dto.ExportRequest;
import com.importexport.dto.ImportRequest;
import com.importexport.dto.ImportResponse;
import com.importexport.service.ImportExportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;

public abstract class BaseImportExportController {

    @Autowired
    protected ImportExportService importExportService;

    protected abstract String getModuleType();

    @PostMapping("/import")
    public ResponseEntity<ImportResponse> importData(
            @RequestParam("file") MultipartFile file,
            @RequestParam(value = "validateOnly", defaultValue = "false") boolean validateOnly,
            @RequestParam(value = "skipErrors", defaultValue = "false") boolean skipErrors,
            @RequestParam(value = "batchSize", defaultValue = "100") int batchSize) {

        ImportRequest request = new ImportRequest();
        request.setModuleType(getModuleType());
        request.setFileName(file.getOriginalFilename());
        request.setValidateOnly(validateOnly);
        request.setSkipErrors(skipErrors);
        request.setBatchSize(batchSize);

        ImportResponse response = importExportService.importData(file, request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/export")
    public ResponseEntity<InputStreamResource> exportData(@RequestBody ExportRequest request) {
        request.setModuleType(getModuleType());
        
        InputStream inputStream = importExportService.exportData(request);
        InputStreamResource resource = new InputStreamResource(inputStream);

        String filename = getModuleType() + "_export." + request.getFileFormat();
        
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename)
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(resource);
    }

    @GetMapping("/template")
    public ResponseEntity<InputStreamResource> getTemplate(
            @RequestParam(value = "format", defaultValue = "csv") String format) {
        
        InputStream inputStream = importExportService.getTemplate(getModuleType(), format);
        InputStreamResource resource = new InputStreamResource(inputStream);

        String filename = getModuleType() + "_template." + format;
        
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename)
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(resource);
    }
}