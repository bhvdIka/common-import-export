package com.importexport.exception;

public class ValidationException extends ImportExportException {

    public ValidationException(String message) {
        super(message);
    }

    public ValidationException(String message, Throwable cause) {
        super(message, cause);
    }
}