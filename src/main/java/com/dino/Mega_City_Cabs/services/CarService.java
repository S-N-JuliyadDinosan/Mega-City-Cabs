package com.dino.Mega_City_Cabs.services;

import com.dino.Mega_City_Cabs.dtos.CarDto;
import com.dino.Mega_City_Cabs.dtos.CarResponseDto;
import com.dino.Mega_City_Cabs.dtos.CarStatusUpdateDto;
import com.dino.Mega_City_Cabs.entities.Car;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CarService {
    Page<Car> getAllCars(Pageable pageable);
    CarResponseDto addCar(CarDto carDto);
    Car getCarById(Long id);
    Car updateCar(Long id, CarDto carDto);
    void deleteCar(Long id);
    Car updateCarStatus(Long id, CarStatusUpdateDto statusDto);
    List<Car> getAvailableCars();
    boolean isCarAvailable(Long id);
}