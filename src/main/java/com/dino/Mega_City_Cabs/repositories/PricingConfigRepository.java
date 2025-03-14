package com.dino.Mega_City_Cabs.repositories;

import com.dino.Mega_City_Cabs.entities.PricingConfig;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PricingConfigRepository extends JpaRepository<PricingConfig, Long> {
}