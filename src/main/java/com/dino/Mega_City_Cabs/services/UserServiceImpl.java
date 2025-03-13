package com.dino.Mega_City_Cabs.services;

import com.dino.Mega_City_Cabs.dtos.LoginDto;
import com.dino.Mega_City_Cabs.dtos.SystemLogDto;
import com.dino.Mega_City_Cabs.entities.User;
import com.dino.Mega_City_Cabs.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private MyUserDetailsService userDetailsService;

    @Autowired
    private TokenBlacklistService tokenBlacklistService;

    @Autowired
    private SystemLogService systemLogService;

    @Autowired
    private UserRepository userRepository;

    @Override
    public String login(LoginDto loginDto) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginDto.getEmail(), loginDto.getPassword())
            );
            UserDetails userDetails = userDetailsService.loadUserByUsername(loginDto.getEmail());
            String token = jwtService.generateToken(userDetails);

            SystemLogDto log = new SystemLogDto();
            log.setActionPerformed("USER_LOGIN");
            Optional<User> optionalUser = userRepository.findByEmail(loginDto.getEmail());
            if (optionalUser.isPresent()) { // Check if user exists
                User user = optionalUser.get(); // Unwrap Optional
                if (user.getCustomer() != null) log.setCustomerId(user.getCustomer().getId());
                if (user.getAdmin() != null) log.setAdminId(user.getAdmin().getId());
                if (user.getDriver() != null) log.setDriverId(user.getDriver().getId());
            }
            systemLogService.logAction(log);

            return token;
        } catch (AuthenticationException e) {
            SystemLogDto log = new SystemLogDto();
            log.setActionPerformed("USER_LOGIN_FAILED");
            log.setLogLevel("ERROR");
            Optional<User> optionalUser = userRepository.findByEmail(loginDto.getEmail());
            if (optionalUser.isPresent() && optionalUser.get().getCustomer() != null) {
                log.setCustomerId(optionalUser.get().getCustomer().getId());
            }
            systemLogService.logAction(log);
            throw new IllegalArgumentException("Invalid email or password");
        } catch (Exception e) {
            SystemLogDto log = new SystemLogDto();
            log.setActionPerformed("USER_LOGIN_ERROR");
            log.setLogLevel("ERROR");
            systemLogService.logAction(log);
            throw new RuntimeException("Failed to process login: " + e.getMessage(), e);
        }
    }

    @Override
    public void logout(String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            SystemLogDto log = new SystemLogDto();
            log.setActionPerformed("USER_LOGOUT_FAILED");
            log.setLogLevel("ERROR");
            systemLogService.logAction(log);
            throw new IllegalArgumentException("Missing or invalid Authorization header");
        }

        String token = authHeader.substring(7);
        tokenBlacklistService.blacklistToken(token);

        SystemLogDto log = new SystemLogDto();
        log.setActionPerformed("USER_LOGOUT");
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Optional<User> optionalUser = userRepository.findByEmail(email);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            if (user.getCustomer() != null) log.setCustomerId(user.getCustomer().getId());
            if (user.getAdmin() != null) log.setAdminId(user.getAdmin().getId());
            if (user.getDriver() != null) log.setDriverId(user.getDriver().getId());
        }
        systemLogService.logAction(log);
    }
}