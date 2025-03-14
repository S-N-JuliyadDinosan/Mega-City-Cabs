package com.dino.Mega_City_Cabs.services;

import com.dino.Mega_City_Cabs.dtos.PricingConfigDto;
import com.dino.Mega_City_Cabs.entities.PricingConfig;
import com.dino.Mega_City_Cabs.repositories.PricingConfigRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PricingServiceImpl implements PricingService {

    @Autowired
    private PricingConfigRepository pricingConfigRepository;

    @Override
    @Transactional
    public PricingConfigDto setPricingConfig(PricingConfigDto pricingConfigDto) {
        try {
            PricingConfig config = new PricingConfig();
            config.setBasePrice(pricingConfigDto.getBasePrice());
            config.setBaseDistanceKm(pricingConfigDto.getBaseDistanceKm());
            config.setAdditionalPricePerKm(pricingConfigDto.getAdditionalPricePerKm());

            PricingConfig savedConfig = pricingConfigRepository.save(config);
            return convertToDto(savedConfig);
        } catch (Exception e) {
            throw new RuntimeException("Failed to set pricing config: " + e.getMessage(), e);
        }
    }

    @Override
    public PricingConfigDto getPricingConfig() {
        PricingConfig config = pricingConfigRepository.findAll().stream()
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Pricing config not set"));
        return convertToDto(config);
    }

    @Override
    public double calculateTotalAmount(double distanceKm) {
        PricingConfig config = pricingConfigRepository.findAll().stream()
                .findFirst()
                .orElse(new PricingConfig(0L, 100.0, 5.0, 20.0)); // Now works with constructor
        if (distanceKm <= config.getBaseDistanceKm()) {
            return config.getBasePrice();
        } else {
            double additionalDistance = distanceKm - config.getBaseDistanceKm();
            return config.getBasePrice() + (additionalDistance * config.getAdditionalPricePerKm());
        }
    }

    private PricingConfigDto convertToDto(PricingConfig config) {
        PricingConfigDto dto = new PricingConfigDto();
        dto.setId(config.getId());
        dto.setBasePrice(config.getBasePrice());
        dto.setBaseDistanceKm(config.getBaseDistanceKm());
        dto.setAdditionalPricePerKm(config.getAdditionalPricePerKm());
        return dto;
    }
}