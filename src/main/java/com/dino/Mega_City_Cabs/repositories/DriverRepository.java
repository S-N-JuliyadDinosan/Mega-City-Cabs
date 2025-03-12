package com.dino.Mega_City_Cabs.repositories;

import com.dino.Mega_City_Cabs.entities.Driver;
import com.dino.Mega_City_Cabs.utils.ValidationMessages;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DriverRepository extends JpaRepository<Driver, Long> {
    boolean existsByNicNumber(@NotBlank(message = ValidationMessages.INVALID_CREDENTIAL) @Pattern(regexp = "^\\d{9}[vV]?$|^\\d{12}$", message = ValidationMessages.INVALID_CREDENTIAL) String nicNumber);

    boolean existsByDrivingLicenseNumber(@NotBlank(message = ValidationMessages.INVALID_CREDENTIAL) @Size(min = 5, max = 20, message = ValidationMessages.MISMATCH_INPUT) String drivingLicenseNumber);
}
