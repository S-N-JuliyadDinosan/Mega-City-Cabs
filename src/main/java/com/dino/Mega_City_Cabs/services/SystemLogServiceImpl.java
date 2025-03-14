package com.dino.Mega_City_Cabs.services;

import com.dino.Mega_City_Cabs.dtos.SystemLogDto;
import com.dino.Mega_City_Cabs.entities.Admin;
import com.dino.Mega_City_Cabs.entities.Customer;
import com.dino.Mega_City_Cabs.entities.Driver;
import com.dino.Mega_City_Cabs.entities.SystemLog;
import com.dino.Mega_City_Cabs.repositories.AdminRepository;
import com.dino.Mega_City_Cabs.repositories.CustomerRepository;
import com.dino.Mega_City_Cabs.repositories.DriverRepository;
import com.dino.Mega_City_Cabs.repositories.SystemLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SystemLogServiceImpl implements SystemLogService {

    @Autowired
    private SystemLogRepository systemLogRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private DriverRepository driverRepository;

    @Override
    @Transactional
    public void logAction(SystemLogDto logDto) {
        SystemLog log = new SystemLog();
        log.setActionPerformed(logDto.getActionPerformed());
        log.setTimeStamp(logDto.getTimeStamp() != null ? logDto.getTimeStamp() : LocalDateTime.now());
        log.setLogLevel(logDto.getLogLevel() != null ? logDto.getLogLevel() : "INFO"); // Map logLevel

        if (logDto.getCustomerId() != null) {
            Customer customer = customerRepository.findById(logDto.getCustomerId())
                    .orElseThrow(() -> new IllegalArgumentException("Customer not found: " + logDto.getCustomerId()));
            log.setCustomer(customer);
        }
        if (logDto.getAdminId() != null) {
            Admin admin = adminRepository.findById(logDto.getAdminId())
                    .orElseThrow(() -> new IllegalArgumentException("Admin not found: " + logDto.getAdminId()));
            log.setAdmin(admin);
        }
        if (logDto.getDriverId() != null) {
            Driver driver = driverRepository.findById(logDto.getDriverId())
                    .orElseThrow(() -> new IllegalArgumentException("Driver not found: " + logDto.getDriverId()));
            log.setDriver(driver);
        }

        systemLogRepository.save(log);
    }

    @Override
    public List<SystemLogDto> getAllLogs() {
        return systemLogRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<SystemLogDto> getLogsByCustomer(Long customerId) {
        return systemLogRepository.findByCustomerId(customerId).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<SystemLogDto> getLogsByAdmin(Long adminId) {
        return systemLogRepository.findByAdminId(adminId).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<SystemLogDto> getLogsByDriver(Long driverId) {
        return systemLogRepository.findByDriverId(driverId).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<SystemLogDto> getLogsByLevel(String logLevel) {
        return systemLogRepository.findByLogLevel(logLevel).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    private SystemLogDto convertToDto(SystemLog log) {
        SystemLogDto dto = new SystemLogDto();
        dto.setId(log.getId());
        dto.setActionPerformed(log.getActionPerformed());
        dto.setTimeStamp(log.getTimeStamp());
        dto.setCustomerId(log.getCustomer() != null ? log.getCustomer().getId() : null);
        dto.setAdminId(log.getAdmin() != null ? log.getAdmin().getId() : null);
        dto.setDriverId(log.getDriver() != null ? log.getDriver().getId() : null);
        dto.setLogLevel(log.getLogLevel()); // Map logLevel back
        return dto;
    }
}