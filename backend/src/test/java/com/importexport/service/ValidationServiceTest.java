package com.importexport.service;

import com.importexport.dto.ImportResponse;
import com.importexport.dto.ValidationError;
import com.importexport.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ValidationServiceTest {

    @Mock
    private CameraRepository cameraRepository;

    @Mock
    private RobotRepository robotRepository;

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private MapRepository mapRepository;

    private ValidationService validationService;

    @BeforeEach
    void setUp() {
        validationService = new ValidationService();
        // Inject mocked repositories using reflection or setters
        setField(validationService, "cameraRepository", cameraRepository);
        setField(validationService, "robotRepository", robotRepository);
        setField(validationService, "taskRepository", taskRepository);
        setField(validationService, "userRepository", userRepository);
        setField(validationService, "mapRepository", mapRepository);
    }

    @Test
    void testValidateData_EmptyData() {
        List<Map<String, Object>> emptyData = new ArrayList<>();
        
        ImportResponse response = validationService.validateData(emptyData, "camera");
        
        assertEquals("VALID", response.getStatus());
        assertEquals(0, response.getTotalRecords());
        assertEquals(0, response.getFailedRecords());
        assertTrue(response.getErrors().isEmpty());
    }

    @Test
    void testValidateData_ValidCameraData() {
        when(cameraRepository.existsByName(anyString())).thenReturn(false);
        when(cameraRepository.existsByIpAddress(anyString())).thenReturn(false);
        
        List<Map<String, Object>> data = new ArrayList<>();
        Map<String, Object> cameraRow = new HashMap<>();
        cameraRow.put("name", "Test Camera");
        cameraRow.put("type", "IP");
        cameraRow.put("ipAddress", "192.168.1.100");
        cameraRow.put("isActive", "true");
        data.add(cameraRow);
        
        ImportResponse response = validationService.validateData(data, "camera");
        
        assertEquals("VALID", response.getStatus());
        assertEquals(1, response.getTotalRecords());
        assertEquals(0, response.getFailedRecords());
        assertTrue(response.getErrors().isEmpty());
    }

    @Test
    void testValidateData_InvalidCameraData() {
        List<Map<String, Object>> data = new ArrayList<>();
        Map<String, Object> cameraRow = new HashMap<>();
        cameraRow.put("name", ""); // Missing required field
        cameraRow.put("type", "INVALID_TYPE"); // Invalid type
        cameraRow.put("ipAddress", "invalid-ip"); // Invalid IP format
        data.add(cameraRow);
        
        ImportResponse response = validationService.validateData(data, "camera");
        
        assertEquals("VALIDATION_ERRORS", response.getStatus());
        assertEquals(1, response.getTotalRecords());
        assertTrue(response.getFailedRecords() > 0);
        assertFalse(response.getErrors().isEmpty());
    }

    @Test
    void testValidateData_DuplicateUserEmail() {
        when(userRepository.existsByEmail("test@example.com")).thenReturn(true);
        
        List<Map<String, Object>> data = new ArrayList<>();
        Map<String, Object> userRow = new HashMap<>();
        userRow.put("username", "testuser");
        userRow.put("email", "test@example.com");
        userRow.put("isActive", "true");
        data.add(userRow);
        
        ImportResponse response = validationService.validateData(data, "user");
        
        assertEquals("VALIDATION_ERRORS", response.getStatus());
        assertEquals(1, response.getTotalRecords());
        assertEquals(1, response.getFailedRecords());
        
        boolean hasDuplicateEmailError = response.getErrors().stream()
                .anyMatch(error -> "DUPLICATE_EMAIL".equals(error.getErrorCode()));
        assertTrue(hasDuplicateEmailError, "Should have duplicate email error");
    }

    @Test
    void testValidateData_UnsupportedModule() {
        List<Map<String, Object>> data = new ArrayList<>();
        Map<String, Object> row = new HashMap<>();
        row.put("name", "test");
        data.add(row);
        
        ImportResponse response = validationService.validateData(data, "unsupported");
        
        assertEquals("VALIDATION_ERRORS", response.getStatus());
        assertEquals(1, response.getFailedRecords());
        
        boolean hasInvalidModuleError = response.getErrors().stream()
                .anyMatch(error -> "INVALID_MODULE".equals(error.getErrorCode()));
        assertTrue(hasInvalidModuleError, "Should have invalid module error");
    }

    // Helper method to set private fields using reflection
    private void setField(Object target, String fieldName, Object value) {
        try {
            var field = target.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(target, value);
        } catch (Exception e) {
            // In a real test, we'd handle this properly
        }
    }
}