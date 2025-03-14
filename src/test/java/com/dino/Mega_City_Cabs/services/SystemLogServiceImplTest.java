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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SystemLogServiceImplTest {

    @Mock
    private SystemLogRepository systemLogRepository;

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private AdminRepository adminRepository;

    @Mock
    private DriverRepository driverRepository;

    @InjectMocks
    private SystemLogServiceImpl systemLogService;

    private SystemLog systemLog;
    private SystemLogDto systemLogDto;
    private Customer customer;
    private Admin admin;
    private Driver driver;

    @BeforeEach
    void setUp() {
        customer = new Customer();
        customer.setId(1L);

        admin = new Admin();
        admin.setId(1L);

        driver = new Driver();
        driver.setId(1L);

        systemLog = new SystemLog();
        systemLog.setId(1L);
        systemLog.setActionPerformed("TEST_ACTION");
        systemLog.setTimeStamp(LocalDateTime.now());
        systemLog.setLogLevel("INFO");
        systemLog.setCustomer(customer);
        systemLog.setAdmin(admin);
        systemLog.setDriver(driver);

        systemLogDto = new SystemLogDto();
        systemLogDto.setActionPerformed("TEST_ACTION");
        systemLogDto.setTimeStamp(LocalDateTime.now());
        systemLogDto.setLogLevel("INFO");
        systemLogDto.setCustomerId(1L);
        systemLogDto.setAdminId(1L);
        systemLogDto.setDriverId(1L);
    }

    @Test
    void logAction_Success_WithAllEntities() {
        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
        when(adminRepository.findById(1L)).thenReturn(Optional.of(admin));
        when(driverRepository.findById(1L)).thenReturn(Optional.of(driver));
        when(systemLogRepository.save(any(SystemLog.class))).thenReturn(systemLog);

        systemLogService.logAction(systemLogDto);

        verify(systemLogRepository, times(1)).save(any(SystemLog.class));
    }

    @Test
    void logAction_Success_WithoutEntities() {
        SystemLogDto minimalDto = new SystemLogDto();
        minimalDto.setActionPerformed("MINIMAL_ACTION");

        when(systemLogRepository.save(any(SystemLog.class))).thenReturn(systemLog);

        systemLogService.logAction(minimalDto);

        verify(systemLogRepository, times(1)).save(any(SystemLog.class));
    }

    @Test
    void logAction_CustomerNotFound_ThrowsException() {
        when(customerRepository.findById(1L)).thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            systemLogService.logAction(systemLogDto);
        });

        assertEquals("Customer not found: 1", exception.getMessage());
        verify(systemLogRepository, never()).save(any(SystemLog.class));
    }

    @Test
    void logAction_AdminNotFound_ThrowsException() {
        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
        when(adminRepository.findById(1L)).thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            systemLogService.logAction(systemLogDto);
        });

        assertEquals("Admin not found: 1", exception.getMessage());
        verify(systemLogRepository, never()).save(any(SystemLog.class));
    }

    @Test
    void logAction_DriverNotFound_ThrowsException() {
        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
        when(adminRepository.findById(1L)).thenReturn(Optional.of(admin));
        when(driverRepository.findById(1L)).thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            systemLogService.logAction(systemLogDto);
        });

        assertEquals("Driver not found: 1", exception.getMessage());
        verify(systemLogRepository, never()).save(any(SystemLog.class));
    }

    @Test
    void getAllLogs_Success() {
        when(systemLogRepository.findAll()).thenReturn(Collections.singletonList(systemLog));

        List<SystemLogDto> result = systemLogService.getAllLogs();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(systemLog.getId(), result.get(0).getId());
        assertEquals(systemLog.getActionPerformed(), result.get(0).getActionPerformed());
    }

    @Test
    void getAllLogs_Empty_ReturnsEmptyList() {
        when(systemLogRepository.findAll()).thenReturn(Collections.emptyList());

        List<SystemLogDto> result = systemLogService.getAllLogs();

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void getLogsByCustomer_Success() {
        when(systemLogRepository.findByCustomerId(1L)).thenReturn(Collections.singletonList(systemLog));

        List<SystemLogDto> result = systemLogService.getLogsByCustomer(1L);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(customer.getId(), result.get(0).getCustomerId());
    }

    @Test
    void getLogsByCustomer_Empty_ReturnsEmptyList() {
        when(systemLogRepository.findByCustomerId(1L)).thenReturn(Collections.emptyList());

        List<SystemLogDto> result = systemLogService.getLogsByCustomer(1L);

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void getLogsByAdmin_Success() {
        when(systemLogRepository.findByAdminId(1L)).thenReturn(Collections.singletonList(systemLog));

        List<SystemLogDto> result = systemLogService.getLogsByAdmin(1L);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(admin.getId(), result.get(0).getAdminId());
    }

    @Test
    void getLogsByAdmin_Empty_ReturnsEmptyList() {
        when(systemLogRepository.findByAdminId(1L)).thenReturn(Collections.emptyList());

        List<SystemLogDto> result = systemLogService.getLogsByAdmin(1L);

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void getLogsByDriver_Success() {
        when(systemLogRepository.findByDriverId(1L)).thenReturn(Collections.singletonList(systemLog));

        List<SystemLogDto> result = systemLogService.getLogsByDriver(1L);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(driver.getId(), result.get(0).getDriverId());
    }

    @Test
    void getLogsByDriver_Empty_ReturnsEmptyList() {
        when(systemLogRepository.findByDriverId(1L)).thenReturn(Collections.emptyList());

        List<SystemLogDto> result = systemLogService.getLogsByDriver(1L);

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void getLogsByLevel_Success() {
        when(systemLogRepository.findByLogLevel("INFO")).thenReturn(Collections.singletonList(systemLog));

        List<SystemLogDto> result = systemLogService.getLogsByLevel("INFO");

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("INFO", result.get(0).getLogLevel());
    }

    @Test
    void getLogsByLevel_Empty_ReturnsEmptyList() {
        when(systemLogRepository.findByLogLevel("ERROR")).thenReturn(Collections.emptyList());

        List<SystemLogDto> result = systemLogService.getLogsByLevel("ERROR");

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }
}