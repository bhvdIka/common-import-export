package com.importexport.service;

import com.importexport.config.FileUploadConfig;
import com.importexport.dto.FileMetadata;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
public class FileService {

    @Autowired
    private FileUploadConfig fileUploadConfig;

    public FileMetadata saveFile(MultipartFile file, String moduleType, String uploadedBy) throws IOException {
        // Generate unique filename
        String originalFilename = file.getOriginalFilename();
        String fileExtension = getFileExtension(originalFilename);
        String uniqueFilename = UUID.randomUUID().toString() + "." + fileExtension;

        // Create upload directory if it doesn't exist
        Path uploadPath = Paths.get(fileUploadConfig.getUploadPath());
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        // Save file
        Path filePath = uploadPath.resolve(uniqueFilename);
        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

        // Create file metadata
        FileMetadata metadata = new FileMetadata(
                uniqueFilename,
                fileExtension,
                file.getSize(),
                file.getContentType(),
                uploadedBy,
                moduleType
        );

        return metadata;
    }

    public void deleteFile(String filename) throws IOException {
        Path filePath = Paths.get(fileUploadConfig.getUploadPath()).resolve(filename);
        if (Files.exists(filePath)) {
            Files.delete(filePath);
        }
    }

    public Path getFilePath(String filename) {
        return Paths.get(fileUploadConfig.getUploadPath()).resolve(filename);
    }

    private String getFileExtension(String filename) {
        if (filename == null || !filename.contains(".")) {
            return "";
        }
        return filename.substring(filename.lastIndexOf(".") + 1).toLowerCase();
    }
}