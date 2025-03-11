package com.dino.Mega_City_Cabs.repositories;

import com.dino.Mega_City_Cabs.entities.Car;
import com.dino.Mega_City_Cabs.enums.CarStatus;
import com.dino.Mega_City_Cabs.utils.ValidationMessages;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CarRepository extends JpaRepository<Car, Long> {
    boolean existsByRegistrationNumber(@NotBlank(message = ValidationMessages.INVALID_CREDENTIAL) @Size(min = 5, max = 20, message = ValidationMessages.MISMATCH_INPUT) String registrationNumber);

    List<Car> findByStatus(CarStatus carStatus);
//    List<Car> findByStatus(Car.CarStatus carStatus);
}
