package com.dino.Mega_City_Cabs.controllers;

import com.dino.Mega_City_Cabs.dtos.CarDto;
import com.dino.Mega_City_Cabs.dtos.CarResponseDto;
import com.dino.Mega_City_Cabs.dtos.CarStatusUpdateDto;
import com.dino.Mega_City_Cabs.entities.Car;
import com.dino.Mega_City_Cabs.enums.RestApiResponseStatusCodes;
import com.dino.Mega_City_Cabs.services.CarService;
import com.dino.Mega_City_Cabs.utils.EndpointBundle;
import com.dino.Mega_City_Cabs.utils.ResponseWrapper;
import com.dino.Mega_City_Cabs.utils.ValidationMessages;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(EndpointBundle.CAR) // "/api/v1/car"
public class CarController {

    @Autowired
    private CarService carService;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ResponseWrapper<Page<Car>>> getAllCars(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        try {
            Pageable pageable = PageRequest.of(page, size);
            Page<Car> cars = carService.getAllCars(pageable);
            return ResponseEntity.ok(new ResponseWrapper<>(
                    RestApiResponseStatusCodes.SUCCESS.getCode(),
                    ValidationMessages.RETRIEVED,
                    cars
            ));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseWrapper<>(
                    RestApiResponseStatusCodes.NOT_FOUND.getCode(),
                    e.getMessage(),
                    null
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseWrapper<>(
                    RestApiResponseStatusCodes.INTERNAL_SERVER_ERROR.getCode(),
                    ValidationMessages.RETRIEVED_FAILED,
                    null
            ));
        }
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ResponseWrapper<CarResponseDto>> addCar(@Valid @RequestBody CarDto carDto) {
        try {
            System.out.println("Received car DTO - Registration: " + carDto.getRegistrationNumber() +
                    ", Model: " + carDto.getModel()); // Safe debug log
            CarResponseDto car = carService.addCar(carDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(new ResponseWrapper<>(
                    RestApiResponseStatusCodes.CREATED.getCode(),
                    ValidationMessages.SAVED_SUCCESSFULL,
                    car
            ));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new ResponseWrapper<>(
                    RestApiResponseStatusCodes.CONFLICT.getCode(),
                    e.getMessage(),
                    null
            ));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseWrapper<>(
                    RestApiResponseStatusCodes.INTERNAL_SERVER_ERROR.getCode(),
                    ValidationMessages.SAVE_FAILED,
                    null
            ));
        }
    }

    @GetMapping(EndpointBundle.ID)
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ResponseWrapper<Car>> getCarById(@PathVariable Long id) {
        try {
            Car car = carService.getCarById(id);
            return ResponseEntity.ok(new ResponseWrapper<>(
                    RestApiResponseStatusCodes.SUCCESS.getCode(),
                    ValidationMessages.RETRIEVED,
                    car
            ));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseWrapper<>(
                    RestApiResponseStatusCodes.NOT_FOUND.getCode(),
                    e.getMessage(),
                    null
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseWrapper<>(
                    RestApiResponseStatusCodes.INTERNAL_SERVER_ERROR.getCode(),
                    ValidationMessages.RETRIEVED_FAILED,
                    null
            ));
        }
    }

    @PutMapping(EndpointBundle.ID)
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ResponseWrapper<Car>> updateCar(@PathVariable Long id, @Valid @RequestBody CarDto carDto) {
        try {
            Car car = carService.updateCar(id, carDto);
            return ResponseEntity.ok(new ResponseWrapper<>(
                    RestApiResponseStatusCodes.SUCCESS.getCode(),
                    ValidationMessages.UPDATE_SUCCESSFUL,
                    car
            ));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new ResponseWrapper<>(
                    RestApiResponseStatusCodes.CONFLICT.getCode(),
                    e.getMessage(),
                    null
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseWrapper<>(
                    RestApiResponseStatusCodes.INTERNAL_SERVER_ERROR.getCode(),
                    ValidationMessages.UPDATE_FAILED,
                    null
            ));
        }
    }

    @DeleteMapping(EndpointBundle.ID)
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ResponseWrapper<String>> deleteCar(@PathVariable Long id) {
        try {
            carService.deleteCar(id);
            return ResponseEntity.ok(new ResponseWrapper<>(
                    RestApiResponseStatusCodes.SUCCESS.getCode(),
                    ValidationMessages.DELETE_SUCCESSFUL,
                    "Car with ID " + id + " deleted successfully"
            ));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new ResponseWrapper<>(
                    RestApiResponseStatusCodes.CONFLICT.getCode(),
                    e.getMessage(),
                    null
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseWrapper<>(
                    RestApiResponseStatusCodes.INTERNAL_SERVER_ERROR.getCode(),
                    ValidationMessages.DELETE_FAILED,
                    null
            ));
        }
    }

    @PatchMapping(EndpointBundle.CAR_STATUS)
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ResponseWrapper<Car>> updateCarStatus(@PathVariable Long id, @Valid @RequestBody CarStatusUpdateDto statusDto) {
        try {
            Car car = carService.updateCarStatus(id, statusDto);
            return ResponseEntity.ok(new ResponseWrapper<>(
                    RestApiResponseStatusCodes.SUCCESS.getCode(),
                    ValidationMessages.UPDATE_SUCCESSFUL,
                    car
            ));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new ResponseWrapper<>(
                    RestApiResponseStatusCodes.CONFLICT.getCode(),
                    e.getMessage(),
                    null
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseWrapper<>(
                    RestApiResponseStatusCodes.INTERNAL_SERVER_ERROR.getCode(),
                    ValidationMessages.UPDATE_FAILED,
                    null
            ));
        }
    }

    @GetMapping(EndpointBundle.GET_AVAILABLE_CARS)
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ResponseWrapper<List<Car>>> getAvailableCars() {
        try {
            List<Car> cars = carService.getAvailableCars();
            return ResponseEntity.ok(new ResponseWrapper<>(
                    RestApiResponseStatusCodes.SUCCESS.getCode(),
                    ValidationMessages.RETRIEVED,
                    cars
            ));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseWrapper<>(
                    RestApiResponseStatusCodes.NOT_FOUND.getCode(),
                    e.getMessage(),
                    null
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseWrapper<>(
                    RestApiResponseStatusCodes.INTERNAL_SERVER_ERROR.getCode(),
                    ValidationMessages.RETRIEVED_FAILED,
                    null
            ));
        }
    }

    @GetMapping(EndpointBundle.SPECIFIC_CAR_AVAILABLE)
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ResponseWrapper<Boolean>> isCarAvailable(@PathVariable Long id) {
        try {
            boolean available = carService.isCarAvailable(id);
            return ResponseEntity.ok(new ResponseWrapper<>(
                    RestApiResponseStatusCodes.SUCCESS.getCode(),
                    ValidationMessages.RETRIEVED,
                    available
            ));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseWrapper<>(
                    RestApiResponseStatusCodes.NOT_FOUND.getCode(),
                    e.getMessage(),
                    null
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseWrapper<>(
                    RestApiResponseStatusCodes.INTERNAL_SERVER_ERROR.getCode(),
                    ValidationMessages.RETRIEVED_FAILED,
                    null
            ));
        }
    }
}