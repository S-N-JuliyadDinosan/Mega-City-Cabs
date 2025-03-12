package com.dino.Mega_City_Cabs.dtos;

import com.dino.Mega_City_Cabs.utils.ValidationMessages;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterDriverDto {
    @NotBlank(message = ValidationMessages.INVALID_NAME)
    @Pattern(regexp = "^[a-zA-Z\\s]+$", message = ValidationMessages.INVALID_NAME)
    @Size(min = 2, max = 50, message = ValidationMessages.MISMATCH_INPUT)
    private String name;

    @NotBlank(message = ValidationMessages.INVALID_CREDENTIAL)
    @Pattern(regexp = "^\\d{9}[vV]?$|^\\d{12}$", message = ValidationMessages.INVALID_CREDENTIAL) // e.g., 123456789V or 123456789012
    private String nicNumber;

    @NotBlank(message = ValidationMessages.INVALID_CREDENTIAL)
    @Size(min = 5, max = 20, message = ValidationMessages.MISMATCH_INPUT)
    private String drivingLicenseNumber;

    @NotBlank(message = ValidationMessages.INVALID_CONTACT_NUMBER)
    @Pattern(regexp = "^\\d{10,15}$", message = ValidationMessages.INVALID_CONTACT_NUMBER)
    private String contactNumber;

    @NotBlank(message = "Availability status is required")
    @Pattern(regexp = "^(AVAILABLE|ON_DUTY)$", message = "Availability status must be AVAILABLE or ON_DUTY")
    private String availabilityStatus; // "AVAILABLE" or "ON_DUTY"

    private boolean havingCar;

    @NotBlank(message = ValidationMessages.PASSWORD_NOT_EMPTY)
    @Size(min = 8, max = 20, message = ValidationMessages.MISMATCH_INPUT)
    private String password; // Added for registration

    private Long userId; // Optional reference to existing User
    private Long adminId; // Required reference to Admin
}