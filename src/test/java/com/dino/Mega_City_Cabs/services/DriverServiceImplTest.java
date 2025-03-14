package com.dino.Mega_City_Cabs.services;

import com.dino.Mega_City_Cabs.dtos.DriverDto;
import com.dino.Mega_City_Cabs.dtos.RegisterDriverDto;
import com.dino.Mega_City_Cabs.entities.Admin;
import com.dino.Mega_City_Cabs.entities.Car;
import com.dino.Mega_City_Cabs.entities.Driver;
import com.dino.Mega_City_Cabs.entities.User;
import com.dino.Mega_City_Cabs.enums.AvailabilityStatus;
import com.dino.Mega_City_Cabs.repositories.AdminRepository;
import com.dino.Mega_City_Cabs.repositories.CarRepository;
import com.dino.Mega_City_Cabs.repositories.DriverRepository;
import com.dino.Mega_City_Cabs.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DriverServiceImplTest {

    @Mock
    private DriverRepository driverRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private AdminRepository adminRepository;

    @Mock
    private CarRepository carRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private DriverServiceImpl driverService;

    private Driver driver;
    private User user;
    private Admin admin;
    private Car car;
    private RegisterDriverDto registerDriverDto;
    private DriverDto driverDto;

    @BeforeEach
    void setUp() {
        admin = new Admin();
        admin.setId(1L);

        user = new User();
        user.setId(1L);
        user.setEmail("NIC123@driver.com");
        user.setPassword("encodedPassword");
        user.setRole("DRIVER");

        driver = new Driver();
        driver.setId(1L);
        driver.setName("John Doe");
        driver.setNicNumber("NIC123");
        driver.setDrivingLicenseNumber("DL123");
        driver.setContactNumber("1234567890");
        driver.setAvailabilityStatus(AvailabilityStatus.AVAILABLE);
        driver.setAdmin(admin);
        driver.setUser(user);

        car = new Car();
        car.setId(1L);

        registerDriverDto = new RegisterDriverDto();
        registerDriverDto.setName("John Doe");
        registerDriverDto.setNicNumber("NIC123");
        registerDriverDto.setDrivingLicenseNumber("DL123");
        registerDriverDto.setContactNumber("1234567890");
        registerDriverDto.setAvailabilityStatus("AVAILABLE");
        registerDriverDto.setAdminId(1L);
        registerDriverDto.setPassword("password123");

        driverDto = new DriverDto();
        driverDto.setName("Jane Doe");
        driverDto.setNicNumber("NIC456");
        driverDto.setDrivingLicenseNumber("DL456");
        driverDto.setContactNumber("0987654321");
        driverDto.setAvailabilityStatus("ON_DUTY");
    }

    @Test
    void getAllDrivers_Success() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Driver> driverPage = new PageImpl<>(Collections.singletonList(driver));
        when(driverRepository.findAll(pageable)).thenReturn(driverPage);

        Page<Driver> result = driverService.getAllDrivers(pageable);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals(driver.getId(), result.getContent().get(0).getId());
    }


    @Test
    void registerDriver_Success() {
        when(driverRepository.existsByNicNumber("NIC123")).thenReturn(false);
        when(driverRepository.existsByDrivingLicenseNumber("DL123")).thenReturn(false);
        when(adminRepository.findById(1L)).thenReturn(Optional.of(admin));
        when(userRepository.existsByEmail("NIC123@driver.com")).thenReturn(false);
        when(passwordEncoder.encode("password123")).thenReturn("encodedPassword");
        when(driverRepository.save(any(Driver.class))).thenReturn(driver);

        Driver result = driverService.registerDriver(registerDriverDto);

        assertNotNull(result);
        assertEquals(driver.getId(), result.getId());
        assertEquals(driver.getNicNumber(), result.getNicNumber());
        verify(driverRepository, times(1)).save(any(Driver.class));
    }

    @Test
    void registerDriver_NicExists_ThrowsException() {
        when(driverRepository.existsByNicNumber("NIC123")).thenReturn(true);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            driverService.registerDriver(registerDriverDto);
        });

        assertEquals("NIC number already exists: NIC123", exception.getMessage());
        verify(driverRepository, never()).save(any(Driver.class));
    }

    @Test
    void registerDriver_LicenseExists_ThrowsException() {
        when(driverRepository.existsByNicNumber("NIC123")).thenReturn(false);
        when(driverRepository.existsByDrivingLicenseNumber("DL123")).thenReturn(true);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            driverService.registerDriver(registerDriverDto);
        });

        assertEquals("Driving license number already exists: DL123", exception.getMessage());
        verify(driverRepository, never()).save(any(Driver.class));
    }

    @Test
    void registerDriver_EmailExists_ThrowsException() {
        when(driverRepository.existsByNicNumber("NIC123")).thenReturn(false);
        when(driverRepository.existsByDrivingLicenseNumber("DL123")).thenReturn(false);
        when(adminRepository.findById(1L)).thenReturn(Optional.of(admin));
        when(userRepository.existsByEmail("NIC123@driver.com")).thenReturn(true);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            driverService.registerDriver(registerDriverDto);
        });

        assertEquals("Email already exists: NIC123@driver.com", exception.getMessage());
        verify(driverRepository, never()).save(any(Driver.class));
    }

    @Test
    void getDriverById_Success() {
        when(driverRepository.findById(1L)).thenReturn(Optional.of(driver));

        Driver result = driverService.getDriverById(1L);

        assertNotNull(result);
        assertEquals(driver.getId(), result.getId());
    }

    @Test
    void getDriverById_NotFound_ThrowsException() {
        when(driverRepository.findById(1L)).thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            driverService.getDriverById(1L);
        });

        assertEquals("Driver not found with ID: 1", exception.getMessage());
    }

    @Test
    void updateDriver_Success() {
        when(driverRepository.findById(1L)).thenReturn(Optional.of(driver));
        when(driverRepository.existsByNicNumber("NIC456")).thenReturn(false);
        when(driverRepository.existsByDrivingLicenseNumber("DL456")).thenReturn(false);
        when(driverRepository.save(driver)).thenReturn(driver);

        Driver result = driverService.updateDriver(1L, driverDto);

        assertNotNull(result);
        assertEquals(driverDto.getName(), result.getName());
        assertEquals(driverDto.getNicNumber(), result.getNicNumber());
        verify(driverRepository, times(1)).save(driver);
    }

    @Test
    void updateDriver_NicExists_ThrowsException() {
        when(driverRepository.findById(1L)).thenReturn(Optional.of(driver));
        when(driverRepository.existsByNicNumber("NIC456")).thenReturn(true);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            driverService.updateDriver(1L, driverDto);
        });

        assertEquals("NIC number already exists: NIC456", exception.getMessage());
        verify(driverRepository, never()).save(any(Driver.class));
    }

    @Test
    void updateDriver_LicenseExists_ThrowsException() {
        when(driverRepository.findById(1L)).thenReturn(Optional.of(driver));
        when(driverRepository.existsByNicNumber("NIC456")).thenReturn(false);
        when(driverRepository.existsByDrivingLicenseNumber("DL456")).thenReturn(true);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            driverService.updateDriver(1L, driverDto);
        });

        assertEquals("Driving license number already exists: DL456", exception.getMessage());
        verify(driverRepository, never()).save(any(Driver.class));
    }

    @Test
    void deleteDriver_Success() {
        when(driverRepository.findById(1L)).thenReturn(Optional.of(driver));

        driverService.deleteDriver(1L);

        verify(driverRepository, times(1)).delete(driver);
    }

    @Test
    void deleteDriver_NotFound_ThrowsException() {
        when(driverRepository.findById(1L)).thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            driverService.deleteDriver(1L);
        });

        assertEquals("Driver not found with ID: 1", exception.getMessage());
        verify(driverRepository, never()).delete(any(Driver.class));
    }

    @Test
    void assignCarToDriver_Success() {
        when(driverRepository.findById(1L)).thenReturn(Optional.of(driver));
        when(carRepository.findById(1L)).thenReturn(Optional.of(car));
        when(driverRepository.save(driver)).thenReturn(driver);

        Driver result = driverService.assignCarToDriver(1L, 1L);

        assertNotNull(result);
        assertEquals(car.getId(), result.getCar().getId());
        verify(driverRepository, times(1)).save(driver);
    }

    @Test
    void assignCarToDriver_DriverNotFound_ThrowsException() {
        when(driverRepository.findById(1L)).thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            driverService.assignCarToDriver(1L, 1L);
        });

        assertEquals("Driver not found with ID: 1", exception.getMessage());
        verify(driverRepository, never()).save(any(Driver.class));
    }

    @Test
    void assignCarToDriver_CarNotFound_ThrowsException() {
        when(driverRepository.findById(1L)).thenReturn(Optional.of(driver));
        when(carRepository.findById(1L)).thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            driverService.assignCarToDriver(1L, 1L);
        });

        assertEquals("Car not found with ID: 1", exception.getMessage());
        verify(driverRepository, never()).save(any(Driver.class));
    }

    @Test
    void assignCarToDriver_AlreadyAssigned_ThrowsException() {
        Car existingCar = new Car();
        existingCar.setId(2L);
        driver.setCar(existingCar);
        when(driverRepository.findById(1L)).thenReturn(Optional.of(driver));
        when(carRepository.findById(1L)).thenReturn(Optional.of(car));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            driverService.assignCarToDriver(1L, 1L);
        });

        assertEquals("Driver already assigned to another car: 2", exception.getMessage());
        verify(driverRepository, never()).save(any(Driver.class));
    }
}