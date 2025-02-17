package com.dino.Mega_City_Cabs.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    private Long id;
    private String userName;
    private String password;
    private String role;  // Customer, Driver, Admin, Manager

    private Long adminManagerId;  // Reference to AdminManager
    private Long customerId;      // Reference to Customer
    private Long driverId;        // Reference to Driver
}
