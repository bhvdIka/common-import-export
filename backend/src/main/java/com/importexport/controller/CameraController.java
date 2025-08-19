package com.importexport.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/camera")
public class CameraController extends BaseImportExportController {

    @Override
    protected String getModuleType() {
        return "camera";
    }
}