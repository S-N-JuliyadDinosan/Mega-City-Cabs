package com.dino.Mega_City_Cabs.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SystemLogDto {
    private Long id;
    private String actionPerformed;
    private LocalDateTime timeStamp;

    private Long customerId;      // Reference to Customer
    private Long adminId;  // Reference to Admin
}
