package com.dino.Mega_City_Cabs.services;

import com.dino.Mega_City_Cabs.dtos.LoginDto;

public interface UserService {
    String login(LoginDto loginDto);
}