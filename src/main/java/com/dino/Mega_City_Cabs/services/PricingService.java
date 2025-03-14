package com.dino.Mega_City_Cabs.services;

import com.dino.Mega_City_Cabs.dtos.PricingConfigDto;

public interface PricingService {
    PricingConfigDto setPricingConfig(PricingConfigDto pricingConfigDto);
    PricingConfigDto getPricingConfig();
    double calculateTotalAmount(double distanceKm);
}