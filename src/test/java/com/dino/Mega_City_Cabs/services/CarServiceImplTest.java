package com.dino.Mega_City_Cabs.services;

import com.dino.Mega_City_Cabs.dtos.CarDto;
import com.dino.Mega_City_Cabs.dtos.CarResponseDto;
import com.dino.Mega_City_Cabs.dtos.CarStatusUpdateDto;
import com.dino.Mega_City_Cabs.entities.Admin;
import com.dino.Mega_City_Cabs.entities.Car;
import com.dino.Mega_City_Cabs.entities.Driver;
import com.dino.Mega_City_Cabs.enums.CarStatus;
import com.dino.Mega_City_Cabs.repositories.AdminRepository;
import com.dino.Mega_City_Cabs.repositories.CarRepository;
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

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CarServiceImplTest {

    @Mock
    private CarRepository carRepository;

    @Mock
    private AdminRepository adminRepository;

    @InjectMocks
    private CarServiceImpl carService;

    private Car car;
    private Admin admin;
    private CarDto carDto;
    private CarStatusUpdateDto statusDto;

    @BeforeEach
    void setUp() {
        admin = new Admin();
        admin.setId(1L);

        car = new Car();
        car.setId(1L);
        car.setRegistrationNumber("ABC123");
        car.setModel("Sedan");
        car.setColor("Black");
        car.setMake("Toyota");
        car.setStatus(CarStatus.AVAILABLE);
        car.setAdmin(admin);

        carDto = new CarDto();
        carDto.setRegistrationNumber("ABC123");
        carDto.setModel("Sedan");
        carDto.setColor("Black");
        carDto.setMake("Toyota");
        carDto.setStatus("AVAILABLE");
        carDto.setAdminId(1L);

        statusDto = new CarStatusUpdateDto();
        statusDto.setStatus("AVAILABLE");
    }

    @Test
    void getAllCars_Success() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Car> carPage = new PageImpl<>(Collections.singletonList(car));
        when(carRepository.findAll(pageable)).thenReturn(carPage);

        Page<Car> result = carService.getAllCars(pageable);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals(car.getId(), result.getContent().get(0).getId());
    }



    @Test
    void addCar_Success() {
        when(carRepository.existsByRegistrationNumber("ABC123")).thenReturn(false);
        when(adminRepository.findById(1L)).thenReturn(Optional.of(admin));
        when(carRepository.save(any(Car.class))).thenReturn(car);

        CarResponseDto result = carService.addCar(carDto);

        assertNotNull(result);
        assertEquals(car.getId(), result.getId());
        assertEquals(car.getRegistrationNumber(), result.getRegistrationNumber());
        verify(carRepository, times(1)).save(any(Car.class));
    }

    @Test
    void addCar_RegistrationExists_ThrowsException() {
        when(carRepository.existsByRegistrationNumber("ABC123")).thenReturn(true);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            carService.addCar(carDto);
        });

        assertEquals("Registration number already exists: ABC123", exception.getMessage());
        verify(carRepository, never()).save(any(Car.class));
    }

    @Test
    void addCar_AdminNotFound_ThrowsException() {
        when(carRepository.existsByRegistrationNumber("ABC123")).thenReturn(false);
        when(adminRepository.findById(1L)).thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            carService.addCar(carDto);
        });

        assertEquals("Admin not found with ID: 1", exception.getMessage());
        verify(carRepository, never()).save(any(Car.class));
    }

    @Test
    void getCarById_Success() {
        when(carRepository.findById(1L)).thenReturn(Optional.of(car));

        Car result = carService.getCarById(1L);

        assertNotNull(result);
        assertEquals(car.getId(), result.getId());
    }

    @Test
    void getCarById_NotFound_ThrowsException() {
        when(carRepository.findById(1L)).thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            carService.getCarById(1L);
        });

        assertEquals("Car not found with ID: 1", exception.getMessage());
    }


    @Test
    void updateCar_RegistrationExists_ThrowsException() {
        Car existingCar = new Car();
        existingCar.setId(1L);
        existingCar.setRegistrationNumber("XYZ789");

        carDto.setRegistrationNumber("ABC123");
        when(carRepository.findById(1L)).thenReturn(Optional.of(existingCar));
        when(carRepository.existsByRegistrationNumber("ABC123")).thenReturn(true);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            carService.updateCar(1L, carDto);
        });

        assertEquals("Registration number already exists: ABC123", exception.getMessage());
        verify(carRepository, never()).save(any(Car.class));
    }

    @Test
    void deleteCar_Success() {
        when(carRepository.findById(1L)).thenReturn(Optional.of(car));

        carService.deleteCar(1L);

        verify(carRepository, times(1)).delete(car);
    }

    @Test
    void deleteCar_AssignedToDriver_ThrowsException() {
        car.setDriver(new Driver());
        when(carRepository.findById(1L)).thenReturn(Optional.of(car));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            carService.deleteCar(1L);
        });

        assertEquals("Cannot delete car assigned to a driver", exception.getMessage());
        verify(carRepository, never()).delete(any(Car.class));
    }

    @Test
    void getAvailableCars_Success() {
        when(carRepository.findByStatus(CarStatus.AVAILABLE)).thenReturn(Collections.singletonList(car));

        List<Car> result = carService.getAvailableCars();

        assertEquals(1, result.size());
        assertEquals(car.getId(), result.get(0).getId());
    }

    @Test
    void isCarAvailable_Success() {
        when(carRepository.findById(1L)).thenReturn(Optional.of(car));

        boolean result = carService.isCarAvailable(1L);

        assertTrue(result);
    }


}