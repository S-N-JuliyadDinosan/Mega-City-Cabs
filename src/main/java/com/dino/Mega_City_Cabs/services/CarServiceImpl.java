package com.dino.Mega_City_Cabs.services;

import com.dino.Mega_City_Cabs.dtos.CarDto;
import com.dino.Mega_City_Cabs.dtos.CarResponseDto;
import com.dino.Mega_City_Cabs.dtos.CarStatusUpdateDto;
import com.dino.Mega_City_Cabs.entities.Admin;
import com.dino.Mega_City_Cabs.entities.Car;
import com.dino.Mega_City_Cabs.enums.CarStatus;
import com.dino.Mega_City_Cabs.repositories.AdminRepository;
import com.dino.Mega_City_Cabs.repositories.CarRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CarServiceImpl implements CarService {

    @Autowired
    private CarRepository carRepository;

    @Autowired
    private AdminRepository adminRepository;

    @Override
    @Transactional(readOnly = true)
    public Page<Car> getAllCars(Pageable pageable) {
        try {
            Page<Car> cars = carRepository.findAll(pageable);
            if (cars.isEmpty()) {
                throw new IllegalArgumentException("No cars found");
            }
            return cars;
        } catch (Exception e) {
            throw new RuntimeException("Failed to retrieve cars: " + e.getMessage(), e);
        }
    }

    @Override
    @Transactional
    public CarResponseDto addCar(CarDto carDto) {
        try {
            if (carRepository.existsByRegistrationNumber(carDto.getRegistrationNumber())) {
                throw new IllegalArgumentException("Registration number already exists: " + carDto.getRegistrationNumber());
            }

            Admin admin = adminRepository.findById(carDto.getAdminId())
                    .orElseThrow(() -> new IllegalArgumentException("Admin not found with ID: " + carDto.getAdminId()));

            Car car = new Car();
            car.setRegistrationNumber(carDto.getRegistrationNumber());
            car.setModel(carDto.getModel());
            car.setColor(carDto.getColor());
            car.setMake(carDto.getMake());
            car.setStatus(CarStatus.valueOf(carDto.getStatus()));
            car.setAdmin(admin);

            // Safe debug logging without toString()
            System.out.println("Saving car - Registration: " + car.getRegistrationNumber() +
                    ", Model: " + car.getModel() +
                    ", Admin ID: " + admin.getId());

            Car savedCar = carRepository.save(car);

            CarResponseDto response = new CarResponseDto();
            response.setId(savedCar.getId());
            response.setRegistrationNumber(savedCar.getRegistrationNumber());
            response.setModel(savedCar.getModel());
            response.setColor(savedCar.getColor());
            response.setMake(savedCar.getMake());
            response.setStatus(savedCar.getStatus().name());
            response.setAdminId(savedCar.getAdmin().getId());

            System.out.println("Car saved - ID: " + response.getId() +
                    ", Registration: " + response.getRegistrationNumber());
            return response;
        } catch (IllegalArgumentException e) {
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to add car: " + e.getMessage(), e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Car getCarById(Long id) {
        try {
            return carRepository.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("Car not found with ID: " + id));
        } catch (IllegalArgumentException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Failed to retrieve car: " + e.getMessage(), e);
        }
    }

    @Override
    @Transactional
    public Car updateCar(Long id, CarDto carDto) {
        try {
            Car car = carRepository.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("Car not found with ID: " + id));

            if (!car.getRegistrationNumber().equals(carDto.getRegistrationNumber()) &&
                    carRepository.existsByRegistrationNumber(carDto.getRegistrationNumber())) {
                throw new IllegalArgumentException("Registration number already exists: " + carDto.getRegistrationNumber());
            }

            car.setRegistrationNumber(carDto.getRegistrationNumber());
            car.setModel(carDto.getModel());
            car.setColor(carDto.getColor());
            car.setStatus(CarStatus.valueOf(carDto.getStatus()));

            return carRepository.save(car);
        } catch (IllegalArgumentException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Failed to update car: " + e.getMessage(), e);
        }
    }

    @Override
    @Transactional
    public void deleteCar(Long id) {
        try {
            Car car = carRepository.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("Car not found with ID: " + id));
            if (car.getDriver() != null) {
                throw new IllegalArgumentException("Cannot delete car assigned to a driver");
            }
            carRepository.delete(car);
        } catch (IllegalArgumentException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Failed to delete car: " + e.getMessage(), e);
        }
    }

    @Override
    @Transactional
    public Car updateCarStatus(Long id, CarStatusUpdateDto statusDto) {
        try {
            Car car = carRepository.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("Car not found with ID: " + id));
            CarStatus newStatus = CarStatus.valueOf(statusDto.getStatus());

            // Validation based on status transition
            if (car.getDriver() == null && newStatus != CarStatus.NOT_ASSIGNED) {
                throw new IllegalArgumentException("Car must be assigned to a driver before setting status to " + newStatus);
            }
            car.setStatus(newStatus);
            return carRepository.save(car);
        } catch (IllegalArgumentException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Failed to update car status: " + e.getMessage(), e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<Car> getAvailableCars() {
        try {
            List<Car> cars = carRepository.findByStatus(CarStatus.AVAILABLE);
            if (cars.isEmpty()) {
                throw new IllegalArgumentException("No available cars found");
            }
            return cars;
        } catch (Exception e) {
            throw new RuntimeException("Failed to retrieve available cars: " + e.getMessage(), e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public boolean isCarAvailable(Long id) {
        try {
            Car car = carRepository.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("Car not found with ID: " + id));
            return car.getStatus() == CarStatus.AVAILABLE;
        } catch (IllegalArgumentException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Failed to check car availability: " + e.getMessage(), e);
        }
    }
}