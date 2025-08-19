package com.importexport.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user")
public class UserController extends BaseImportExportController {

    @Override
    protected String getModuleType() {
        return "user";
    }
}