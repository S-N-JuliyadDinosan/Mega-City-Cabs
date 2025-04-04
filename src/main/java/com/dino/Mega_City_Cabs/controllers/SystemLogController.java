package com.dino.Mega_City_Cabs.controllers;

import com.dino.Mega_City_Cabs.dtos.SystemLogDto;
import com.dino.Mega_City_Cabs.services.SystemLogService;
import com.dino.Mega_City_Cabs.utils.EndpointBundle;
import com.dino.Mega_City_Cabs.utils.ResponseWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(EndpointBundle.SYSTEM_LOGS)
public class SystemLogController {

    @Autowired
    private SystemLogService systemLogService;

    @GetMapping
    public ResponseEntity<ResponseWrapper<List<SystemLogDto>>> getAllLogs() {
        List<SystemLogDto> logs = systemLogService.getAllLogs();
        return ResponseEntity.ok(new ResponseWrapper<>(200, "Logs retrieved successfully", logs));
    }

    @GetMapping(EndpointBundle.GET_LOGS_BY_CUSTOMER)
    public ResponseEntity<ResponseWrapper<List<SystemLogDto>>> getLogsByCustomer(@PathVariable Long customerId) {
        List<SystemLogDto> logs = systemLogService.getLogsByCustomer(customerId);
        return ResponseEntity.ok(new ResponseWrapper<>(200, "Customer logs retrieved successfully", logs));
    }

    @GetMapping(EndpointBundle.GET_LOGS_BY_ADMIN)
    public ResponseEntity<ResponseWrapper<List<SystemLogDto>>> getLogsByAdmin(@PathVariable Long adminId) {
        List<SystemLogDto> logs = systemLogService.getLogsByAdmin(adminId);
        return ResponseEntity.ok(new ResponseWrapper<>(200, "Admin logs retrieved successfully", logs));
    }

    @GetMapping(EndpointBundle.GET_LOGS_BY_DRIVER)
    public ResponseEntity<ResponseWrapper<List<SystemLogDto>>> getLogsByDriver(@PathVariable Long driverId) {
        List<SystemLogDto> logs = systemLogService.getLogsByDriver(driverId);
        return ResponseEntity.ok(new ResponseWrapper<>(200, "Driver logs retrieved successfully", logs));
    }

    @GetMapping(EndpointBundle.GET_LOGS_BY_LEVEL)
    public ResponseEntity<ResponseWrapper<List<SystemLogDto>>> getLogsByLevel(@PathVariable String logLevel) {
        List<SystemLogDto> logs = systemLogService.getLogsByLevel(logLevel);
        return ResponseEntity.ok(new ResponseWrapper<>(200, "Logs by level retrieved successfully", logs));
    }
}