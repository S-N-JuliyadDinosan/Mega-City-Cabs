package com.dino.Mega_City_Cabs.dtos;

import com.dino.Mega_City_Cabs.utils.ValidationMessages;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AdminDto {
    private Long id;

    @NotBlank(message = ValidationMessages.INVALID_NAME)
    @Pattern(regexp = "^[a-zA-Z\\s]+$", message = ValidationMessages.INVALID_NAME)
    @Size(min = 2, max = 50, message = ValidationMessages.MISMATCH_INPUT)
    private String name;

    @NotBlank(message = ValidationMessages.INVALID_CONTACT_NUMBER)
    @Pattern(regexp = "^\\d{10,15}$", message = ValidationMessages.INVALID_CONTACT_NUMBER)
    private String contactNumber;

    @NotBlank(message = ValidationMessages.EMAIL_NOT_EMPTY)
    @Email(message = ValidationMessages.INVALID_EMAIL)
    private String email;

    @NotBlank(message = ValidationMessages.PASSWORD_NOT_EMPTY)
    @Size(min = 8, max = 20, message = ValidationMessages.MISMATCH_INPUT)
    private String password;

    // Used for update to avoid forcing password change
    public AdminDto() {}
}