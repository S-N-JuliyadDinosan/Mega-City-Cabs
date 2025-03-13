package com.dino.Mega_City_Cabs.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookingResponseDto {
    private Long id;
    private String pickUpLocation;
    private String destinationDetails;
    private LocalDateTime bookingDateTime;
    private String status;
    private Long customerId;
    private Long driverId;
    private Long carId;
    private Long billingId;
    private Double distanceKm;
    private Double totalAmount;
}