package com.dino.Mega_City_Cabs.services;

import com.dino.Mega_City_Cabs.dtos.BillingDto;

public interface BillingService {
    BillingDto createBilling(Long bookingId, String paymentMethod);
    BillingDto getBillingById(Long id);
}