package com.dino.Mega_City_Cabs.controllers;

import com.dino.Mega_City_Cabs.dtos.SystemLogDto;
import com.dino.Mega_City_Cabs.services.SystemLogService;
import com.dino.Mega_City_Cabs.utils.ResponseWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/v1/logs")
public class SystemLogController {

    @Autowired
    private SystemLogService systemLogService;

    @GetMapping
    public ResponseEntity<ResponseWrapper<List<SystemLogDto>>> getAllLogs() {
        List<SystemLogDto> logs = systemLogService.getAllLogs();
        return ResponseEntity.ok(new ResponseWrapper<>(200, "Logs retrieved successfully", logs));
    }

    @GetMapping("/customer/{customerId}")
    public ResponseEntity<ResponseWrapper<List<SystemLogDto>>> getLogsByCustomer(@PathVariable Long customerId) {
        List<SystemLogDto> logs = systemLogService.getLogsByCustomer(customerId);
        return ResponseEntity.ok(new ResponseWrapper<>(200, "Customer logs retrieved successfully", logs));
    }

    @GetMapping("/admin/{adminId}")
    public ResponseEntity<ResponseWrapper<List<SystemLogDto>>> getLogsByAdmin(@PathVariable Long adminId) {
        List<SystemLogDto> logs = systemLogService.getLogsByAdmin(adminId);
        return ResponseEntity.ok(new ResponseWrapper<>(200, "Admin logs retrieved successfully", logs));
    }

    @GetMapping("/driver/{driverId}")
    public ResponseEntity<ResponseWrapper<List<SystemLogDto>>> getLogsByDriver(@PathVariable Long driverId) {
        List<SystemLogDto> logs = systemLogService.getLogsByDriver(driverId);
        return ResponseEntity.ok(new ResponseWrapper<>(200, "Driver logs retrieved successfully", logs));
    }

    @GetMapping("/level/{logLevel}")
    public ResponseEntity<ResponseWrapper<List<SystemLogDto>>> getLogsByLevel(@PathVariable String logLevel) {
        List<SystemLogDto> logs = systemLogService.getLogsByLevel(logLevel);
        return ResponseEntity.ok(new ResponseWrapper<>(200, "Logs by level retrieved successfully", logs));
    }
}