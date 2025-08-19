package com.importexport.util;

import java.util.regex.Pattern;

public class ValidationUtil {

    private static final Pattern EMAIL_PATTERN = 
            Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");
    
    private static final Pattern IP_ADDRESS_PATTERN = 
            Pattern.compile("^((25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$");

    public static boolean isValidEmail(String email) {
        return email != null && EMAIL_PATTERN.matcher(email.trim()).matches();
    }

    public static boolean isValidIpAddress(String ipAddress) {
        return ipAddress != null && IP_ADDRESS_PATTERN.matcher(ipAddress.trim()).matches();
    }

    public static boolean isValidBoolean(String value) {
        if (value == null) return false;
        String trimmedValue = value.trim().toLowerCase();
        return trimmedValue.equals("true") || trimmedValue.equals("false") || 
               trimmedValue.equals("1") || trimmedValue.equals("0") ||
               trimmedValue.equals("yes") || trimmedValue.equals("no");
    }

    public static boolean parseBoolean(String value) {
        if (value == null) return false;
        String trimmedValue = value.trim().toLowerCase();
        return trimmedValue.equals("true") || trimmedValue.equals("1") || trimmedValue.equals("yes");
    }

    public static boolean isValidInteger(String value) {
        if (value == null || value.trim().isEmpty()) return false;
        try {
            Integer.parseInt(value.trim());
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static boolean isValidDouble(String value) {
        if (value == null || value.trim().isEmpty()) return false;
        try {
            Double.parseDouble(value.trim());
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static boolean isValidLong(String value) {
        if (value == null || value.trim().isEmpty()) return false;
        try {
            Long.parseLong(value.trim());
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static boolean isNotEmpty(String value) {
        return value != null && !value.trim().isEmpty();
    }

    public static boolean isValidLength(String value, int maxLength) {
        return value == null || value.length() <= maxLength;
    }

    public static boolean isValidRange(int value, int min, int max) {
        return value >= min && value <= max;
    }

    public static boolean isValidRange(double value, double min, double max) {
        return value >= min && value <= max;
    }
}