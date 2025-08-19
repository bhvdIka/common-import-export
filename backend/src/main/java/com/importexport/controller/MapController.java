package com.importexport.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/map")
public class MapController extends BaseImportExportController {

    @Override
    protected String getModuleType() {
        return "map";
    }
}