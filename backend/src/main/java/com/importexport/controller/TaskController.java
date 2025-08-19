package com.importexport.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/task")
public class TaskController extends BaseImportExportController {

    @Override
    protected String getModuleType() {
        return "task";
    }
}