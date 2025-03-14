package com.dino.Mega_City_Cabs.services;

import com.dino.Mega_City_Cabs.dtos.SystemLogDto;

import java.util.List;

public interface SystemLogService {
    void logAction(SystemLogDto logDto);
    List<SystemLogDto> getAllLogs();
    List<SystemLogDto> getLogsByCustomer(Long customerId);
    List<SystemLogDto> getLogsByAdmin(Long adminId);
    List<SystemLogDto> getLogsByDriver(Long driverId);
    List<SystemLogDto> getLogsByLevel(String logLevel);
}