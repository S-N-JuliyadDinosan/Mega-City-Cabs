package com.dino.Mega_City_Cabs.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookingDto {
    private Long id;
    private String pickUpLocation;
    private String designationDetails;
    private LocalDateTime bookingDateTime;
    private String status;

    private Long customerId; // Reference to Customer
    private Long driverId;   // Reference to Driver
    private Long carId;      // Reference to Car
    private Long billingId;  //Reference to Billing
}
