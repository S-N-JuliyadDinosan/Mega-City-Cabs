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
public class CarDto {
    private Long id;

    @NotBlank(message = ValidationMessages.INVALID_CREDENTIAL)
    @Size(min = 5, max = 20, message = ValidationMessages.MISMATCH_INPUT)
    private String registrationNumber;

    @NotBlank(message = ValidationMessages.INVALID_NAME)
    @Size(min = 2, max = 50, message = ValidationMessages.MISMATCH_INPUT)
    private String model;

    @NotBlank(message = ValidationMessages.INVALID_NAME)
    @Size(min = 3, max = 30, message = ValidationMessages.MISMATCH_INPUT)
    private String color;

    @NotBlank(message = ValidationMessages.INVALID_NAME)
    @Size(min = 2, max = 30, message = ValidationMessages.MISMATCH_INPUT)
    private String make;

    @NotBlank(message = "Car status is required")
    @Pattern(regexp = "^(NOT_ASSIGNED|AVAILABLE|BOOKED|MAINTENANCE)$", message = "Invalid car status")
    private String status;

    private Long adminId; // Required for creation
}