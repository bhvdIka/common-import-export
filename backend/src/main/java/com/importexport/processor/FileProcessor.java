package com.importexport.processor;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

public interface FileProcessor {

    /**
     * Process the input stream and return list of data maps
     * @param inputStream The file input stream
     * @return List of data maps representing rows
     * @throws Exception if processing fails
     */
    List<Map<String, Object>> processFile(InputStream inputStream) throws Exception;

    /**
     * Get supported file extensions
     * @return Array of supported file extensions
     */
    String[] getSupportedExtensions();

    /**
     * Check if the processor supports the given file type
     * @param fileType The file type to check
     * @return true if supported, false otherwise
     */
    boolean supports(String fileType);
}