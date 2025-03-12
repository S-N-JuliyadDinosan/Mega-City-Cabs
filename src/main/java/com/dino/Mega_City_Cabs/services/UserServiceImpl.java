package com.dino.Mega_City_Cabs.services;

import com.dino.Mega_City_Cabs.dtos.LoginDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private MyUserDetailsService userDetailsService;

    @Override
    public String login(LoginDto loginDto) {
        try {
            // Authenticate the user
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginDto.getEmail(), loginDto.getPassword())
            );

            // Load UserDetails
            UserDetails userDetails = userDetailsService.loadUserByUsername(loginDto.getEmail());

            // Generate JWT token
            return jwtService.generateToken(userDetails);
        } catch (AuthenticationException e) {
            throw new IllegalArgumentException("Invalid email or password");
        } catch (Exception e) {
            throw new RuntimeException("Failed to process login: " + e.getMessage(), e);
        }
    }
}