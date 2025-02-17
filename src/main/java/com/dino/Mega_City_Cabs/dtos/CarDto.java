package com.dino.Mega_City_Cabs.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CarDto {
    private Long id;
    private String regNumber;
    private String model;
    private String make;
    private String yearOfManufacture;
    private String status;   // Available, Booked, Maintenance

    private Long driverId;       // Reference to Driver
    private Long adminManagerId; // Reference to AdminManager
    private List<Long> bookingIds; // List of Booking IDs


}
