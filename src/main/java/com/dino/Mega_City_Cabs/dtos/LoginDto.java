package com.dino.Mega_City_Cabs.dtos;

import com.dino.Mega_City_Cabs.utils.ValidationMessages;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginDto {
    @NotBlank(message = ValidationMessages.EMAIL_NOT_EMPTY)
    @Email(message = ValidationMessages.INVALID_EMAIL)
    private String email;

    @NotBlank(message = ValidationMessages.PASSWORD_NOT_EMPTY)
    private String password;
}