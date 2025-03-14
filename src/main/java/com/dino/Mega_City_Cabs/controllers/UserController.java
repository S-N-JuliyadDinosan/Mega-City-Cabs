package com.dino.Mega_City_Cabs.controllers;

import com.dino.Mega_City_Cabs.dtos.LoginDto;
import com.dino.Mega_City_Cabs.enums.RestApiResponseStatusCodes;
import com.dino.Mega_City_Cabs.services.UserService;
import com.dino.Mega_City_Cabs.utils.EndpointBundle;
import com.dino.Mega_City_Cabs.utils.ResponseWrapper;
import com.dino.Mega_City_Cabs.utils.ValidationMessages;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(EndpointBundle.USER) // "/api/v1/users"
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping(EndpointBundle.LOGIN_USER)
    public ResponseEntity<ResponseWrapper<String>> login(@Valid @RequestBody LoginDto loginDto) {
        try {
            String token = userService.login(loginDto);
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseWrapper<>(
                    RestApiResponseStatusCodes.SUCCESS.getCode(),
                    ValidationMessages.RETRIEVED,
                    token
            ));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ResponseWrapper<>(
                    RestApiResponseStatusCodes.CONFLICT.getCode(),
                    e.getMessage(),
                    null
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseWrapper<>(
                    RestApiResponseStatusCodes.INTERNAL_SERVER_ERROR.getCode(),
                    ValidationMessages.RETRIEVED_FAILED,
                    null
            ));
        }
    }

    @PostMapping(EndpointBundle.LOGOUT_USER)
    public ResponseEntity<ResponseWrapper<String>> logout(@RequestHeader("Authorization") String authHeader) {
        try {
            userService.logout(authHeader);
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseWrapper<>(
                    200, // Adjust to your success code
                    "Logged out successfully",
                    null
            ));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseWrapper<>(
                    400, // Adjust to your error code
                    e.getMessage(),
                    null
            ));
        }
    }
}