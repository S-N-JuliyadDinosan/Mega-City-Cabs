package com.dino.Mega_City_Cabs.dtos;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SystemLogDto {
    private Long id;

    @NotBlank(message = "Action performed is required")
    private String actionPerformed;

    private LocalDateTime timeStamp;

    private Long customerId; // Reference to Customer
    private Long adminId;    // Reference to Admin
    private Long driverId;   // Reference to Driver

    private String logLevel; // Added for log level (e.g., "INFO", "ERROR")
}