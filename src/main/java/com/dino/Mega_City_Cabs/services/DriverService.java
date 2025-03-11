package com.dino.Mega_City_Cabs.services;

import com.dino.Mega_City_Cabs.dtos.DriverDto;
import com.dino.Mega_City_Cabs.dtos.RegisterDriverDto;
import com.dino.Mega_City_Cabs.entities.Driver;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface DriverService {
    Page<Driver> getAllDrivers(Pageable pageable); // Updated to return Page with Pageable
    Driver registerDriver(RegisterDriverDto driverDto);
    Driver getDriverById(Long id);
    Driver updateDriver(Long id, DriverDto driverDto);
    void deleteDriver(Long id);
    Driver assignCarToDriver(Long driverId, Long carId);
}