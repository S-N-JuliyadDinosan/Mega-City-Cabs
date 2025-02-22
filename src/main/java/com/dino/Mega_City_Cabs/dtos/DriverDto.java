package com.dino.Mega_City_Cabs.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DriverDto {
    private Long id;
    private String name;
    private String nicNumber;
    private String drivingLicenseNumber;
    private String contactNumber;
    private String availabilityStatus;  // Available, On duty
    private boolean havingCar;

    private Long userId;             // Reference to User
    private List<Long> carIds;       // List of Car IDs
    private List<Long> bookingIds;   // List of Booking IDs
    private Long adminId;     // Reference to Admin

}
