package com.importexport.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/robot")
public class RobotController extends BaseImportExportController {

    @Override
    protected String getModuleType() {
        return "robot";
    }
}