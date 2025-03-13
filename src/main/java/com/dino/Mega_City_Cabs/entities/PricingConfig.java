package com.dino.Mega_City_Cabs.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PricingConfig {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private double basePrice; // e.g., 100 INR
    private double baseDistanceKm; // e.g., 5 km
    private double additionalPricePerKm; // e.g., 20 INR/km
}