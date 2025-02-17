package com.dino.Mega_City_Cabs.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BillingDto {
    private Long id;
    private BigDecimal totalAmount;
    private BigDecimal taxAmount;
    private BigDecimal discount;
    private String paymentMethod;

    private Long customerId; // Reference to Customer
    private Long bookingId; // Reference to Booking

}
