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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class DriverServiceImpl implements DriverService {

    @Autowired
    private DriverRepository driverRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private CarRepository carRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    @Transactional(readOnly = true)
    public Page<Driver> getAllDrivers(Pageable pageable) {
        try {
            Page<Driver> drivers = driverRepository.findAll(pageable);
            if (drivers.isEmpty()) {
                throw new IllegalArgumentException("No drivers found");
            }
            return drivers;
        } catch (Exception e) {
            throw new RuntimeException("Failed to retrieve drivers: " + e.getMessage(), e);
        }
    }

    @Override
    @Transactional
    public Driver registerDriver(RegisterDriverDto driverDto) {
        try {
            // Check for duplicate NIC or license
            if (driverRepository.existsByNicNumber(driverDto.getNicNumber())) {
                throw new IllegalArgumentException("NIC number already exists: " + driverDto.getNicNumber());
            }
            if (driverRepository.existsByDrivingLicenseNumber(driverDto.getDrivingLicenseNumber())) {
                throw new IllegalArgumentException("Driving license number already exists: " + driverDto.getDrivingLicenseNumber());
            }

            // Fetch Admin
            Admin admin = adminRepository.findById(driverDto.getAdminId())
                    .orElseThrow(() -> new IllegalArgumentException("Admin not found with ID: " + driverDto.getAdminId()));

            // Create Driver first
            Driver driver = new Driver();
            driver.setName(driverDto.getName());
            driver.setNicNumber(driverDto.getNicNumber());
            driver.setDrivingLicenseNumber(driverDto.getDrivingLicenseNumber());
            driver.setContactNumber(driverDto.getContactNumber());
            driver.setAvailabilityStatus(AvailabilityStatus.valueOf(driverDto.getAvailabilityStatus()));
            driver.setAdmin(admin);

            // Create User for Driver
            String email = driverDto.getUserId() != null
                    ? userRepository.findById(driverDto.getUserId())
                    .orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + driverDto.getUserId()))
                    .getEmail()
                    : driverDto.getNicNumber() + "@driver.com";

            if (userRepository.existsByEmail(email)) {
                throw new IllegalArgumentException("Email already exists: " + email);
            }

            User user = User.createUser(
                    email,
                    passwordEncoder.encode(driverDto.getPassword()),
                    "DRIVER",
                    driver // Pass the Driver entity instead of null
            );

            // Set bidirectional relationship
            driver.setUser(user);

            // Save Driver (cascades to User)
            return driverRepository.save(driver);
        } catch (IllegalArgumentException e) {
            throw e;
        } catch (Exception e) {
            System.out.println("Error registering driver: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Failed to register driver: " + e.getMessage(), e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Driver getDriverById(Long id) {
        try {
            return driverRepository.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("Driver not found with ID: " + id));
        } catch (IllegalArgumentException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Failed to retrieve driver: " + e.getMessage(), e);
        }
    }

    @Override
    @Transactional
    public Driver updateDriver(Long id, DriverDto driverDto) {
        try {
            Driver driver = driverRepository.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("Driver not found with ID: " + id));

            // Update fields
            driver.setName(driverDto.getName());
            if (!driver.getNicNumber().equals(driverDto.getNicNumber()) && driverRepository.existsByNicNumber(driverDto.getNicNumber())) {
                throw new IllegalArgumentException("NIC number already exists: " + driverDto.getNicNumber());
            }
            driver.setNicNumber(driverDto.getNicNumber());
            if (!driver.getDrivingLicenseNumber().equals(driverDto.getDrivingLicenseNumber()) && driverRepository.existsByDrivingLicenseNumber(driverDto.getDrivingLicenseNumber())) {
                throw new IllegalArgumentException("Driving license number already exists: " + driverDto.getDrivingLicenseNumber());
            }
            driver.setDrivingLicenseNumber(driverDto.getDrivingLicenseNumber());
            driver.setContactNumber(driverDto.getContactNumber());
            driver.setAvailabilityStatus(AvailabilityStatus.valueOf(driverDto.getAvailabilityStatus()));

            return driverRepository.save(driver);
        } catch (IllegalArgumentException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Failed to update driver: " + e.getMessage(), e);
        }
    }

    @Override
    @Transactional
    public void deleteDriver(Long id) {
        try {
            Driver driver = driverRepository.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("Driver not found with ID: " + id));
            driverRepository.delete(driver);
        } catch (IllegalArgumentException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Failed to delete driver: " + e.getMessage(), e);
        }
    }

    @Override
    @Transactional
    public Driver assignCarToDriver(Long driverId, Long carId) {
        try {
            Driver driver = driverRepository.findById(driverId)
                    .orElseThrow(() -> new IllegalArgumentException("Driver not found with ID: " + driverId));
            Car car = carRepository.findById(carId)
                    .orElseThrow(() -> new IllegalArgumentException("Car not found with ID: " + carId));

            if (driver.getCar() != null && !driver.getCar().getId().equals(carId)) {
                throw new IllegalArgumentException("Driver already assigned to another car: " + driver.getCar().getId());
            }

            driver.setCar(car);
            return driverRepository.save(driver);
        } catch (IllegalArgumentException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Failed to assign car to driver: " + e.getMessage(), e);
        }
    }
}