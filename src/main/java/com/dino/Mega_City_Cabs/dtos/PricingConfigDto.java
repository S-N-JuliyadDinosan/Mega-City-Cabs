package com.dino.Mega_City_Cabs.dtos;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class PricingConfigDto {
    private Long id;

    @NotNull(message = "Base price is required")
    @Min(value = 0, message = "Base price cannot be negative")
    private Double basePrice;

    @NotNull(message = "Base distance is required")
    @Min(value = 1, message = "Base distance must be at least 1 km")
    private Double baseDistanceKm;

    @NotNull(message = "Additional price per km is required")
    @Min(value = 0, message = "Additional price cannot be negative")
    private Double additionalPricePerKm;
}