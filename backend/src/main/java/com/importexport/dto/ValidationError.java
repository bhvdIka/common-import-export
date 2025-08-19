package com.importexport.dto;

public class ValidationError {

    private int row;
    private String field;
    private String errorCode;
    private String errorMessage;
    private String actualValue;
    private String expectedValue;

    // Constructors
    public ValidationError() {}

    public ValidationError(int row, String field, String errorCode, String errorMessage) {
        this.row = row;
        this.field = field;
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    public ValidationError(int row, String field, String errorCode, String errorMessage, String actualValue, String expectedValue) {
        this(row, field, errorCode, errorMessage);
        this.actualValue = actualValue;
        this.expectedValue = expectedValue;
    }

    // Getters and Setters
    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getActualValue() {
        return actualValue;
    }

    public void setActualValue(String actualValue) {
        this.actualValue = actualValue;
    }

    public String getExpectedValue() {
        return expectedValue;
    }

    public void setExpectedValue(String expectedValue) {
        this.expectedValue = expectedValue;
    }
}