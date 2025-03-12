package com.dino.Mega_City_Cabs.dtos;

import com.dino.Mega_City_Cabs.utils.ValidationMessages;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CarStatusUpdateDto {
    @NotBlank(message = "Car status is required")
    @Pattern(regexp = "^(NOT_ASSIGNED|AVAILABLE|BOOKED|MAINTENANCE)$", message = "Invalid car status")
    private String status;
}