package com.dino.Mega_City_Cabs.services;

import com.dino.Mega_City_Cabs.dtos.LoginDto;
import com.dino.Mega_City_Cabs.dtos.UserDto;

public interface UserService {
    String login(LoginDto loginDto);
    void logout(String authHeader);
}