package com.dino.Mega_City_Cabs.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerDto {
    private Long id;
    private String name;
    private String address;
    private String nicNumber;
    private String phoneNumber;

    private Long userId;             // Reference to User
    private List<Long> bookingIds;   // List of Booking IDs
    private List<Long> billingIds;   // List of Billing IDs
    private List<Long> systemLogIds; // List of System Log IDs

}

