package com.importexport.util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileUtil {

    public static String getFileExtension(String filename) {
        if (filename == null || !filename.contains(".")) {
            return "";
        }
        return filename.substring(filename.lastIndexOf(".") + 1).toLowerCase();
    }

    public static boolean isValidFileType(String filename, String[] allowedTypes) {
        String extension = getFileExtension(filename);
        for (String type : allowedTypes) {
            if (type.equalsIgnoreCase(extension)) {
                return true;
            }
        }
        return false;
    }

    public static void createDirectoryIfNotExists(String directoryPath) throws IOException {
        Path path = Paths.get(directoryPath);
        if (!Files.exists(path)) {
            Files.createDirectories(path);
        }
    }

    public static String sanitizeFilename(String filename) {
        if (filename == null) {
            return "unnamed_file";
        }
        return filename.replaceAll("[^a-zA-Z0-9._-]", "_");
    }

    public static long getFileSize(Path filePath) throws IOException {
        return Files.size(filePath);
    }

    public static boolean fileExists(String filePath) {
        return Files.exists(Paths.get(filePath));
    }
}