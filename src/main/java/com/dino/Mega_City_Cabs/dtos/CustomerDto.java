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
public class CustomerDto {
    private Long id;

    @NotBlank(message = ValidationMessages.INVALID_NAME)
    @Size(min = 2, max = 50, message = ValidationMessages.MISMATCH_INPUT)
    private String name;

    @NotBlank(message = "Address is required")
    @Size(min = 5, max = 100, message = ValidationMessages.MISMATCH_INPUT)
    private String address;

    @NotBlank(message = ValidationMessages.INVALID_CREDENTIAL)
    @Size(min = 9, max = 15, message = ValidationMessages.MISMATCH_INPUT)
    private String nicNumber;

    @NotBlank(message = "Phone number is required")
    @Pattern(regexp = "^\\d{10,15}$", message = "Invalid phone number")
    private String phoneNumber;
}