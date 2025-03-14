package com.dino.Mega_City_Cabs.services;

import com.dino.Mega_City_Cabs.dtos.PricingConfigDto;
import com.dino.Mega_City_Cabs.entities.PricingConfig;
import com.dino.Mega_City_Cabs.repositories.PricingConfigRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PricingServiceImplTest {

    @Mock
    private PricingConfigRepository pricingConfigRepository;

    @InjectMocks
    private PricingServiceImpl pricingService;

    private PricingConfig pricingConfig;
    private PricingConfigDto pricingConfigDto;

    @BeforeEach
    void setUp() {
        pricingConfig = new PricingConfig(1L, 100.0, 5.0, 20.0); // Assuming constructor: (id, basePrice, baseDistanceKm, additionalPricePerKm)
        pricingConfigDto = new PricingConfigDto();
        pricingConfigDto.setBasePrice(100.0);
        pricingConfigDto.setBaseDistanceKm(5.0);
        pricingConfigDto.setAdditionalPricePerKm(20.0);
    }

    @Test
    void setPricingConfig_Success() {
        when(pricingConfigRepository.save(any(PricingConfig.class))).thenReturn(pricingConfig);

        PricingConfigDto result = pricingService.setPricingConfig(pricingConfigDto);

        assertNotNull(result);
        assertEquals(pricingConfig.getId(), result.getId());
        assertEquals(pricingConfig.getBasePrice(), result.getBasePrice());
        assertEquals(pricingConfig.getBaseDistanceKm(), result.getBaseDistanceKm());
        assertEquals(pricingConfig.getAdditionalPricePerKm(), result.getAdditionalPricePerKm());
        verify(pricingConfigRepository, times(1)).save(any(PricingConfig.class));
    }

    @Test
    void setPricingConfig_Exception_ThrowsRuntimeException() {
        when(pricingConfigRepository.save(any(PricingConfig.class))).thenThrow(new RuntimeException("Database error"));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            pricingService.setPricingConfig(pricingConfigDto);
        });

        assertEquals("Failed to set pricing config: Database error", exception.getMessage());
        verify(pricingConfigRepository, times(1)).save(any(PricingConfig.class));
    }

    @Test
    void getPricingConfig_Success() {
        when(pricingConfigRepository.findAll()).thenReturn(Collections.singletonList(pricingConfig));

        PricingConfigDto result = pricingService.getPricingConfig();

        assertNotNull(result);
        assertEquals(pricingConfig.getId(), result.getId());
        assertEquals(pricingConfig.getBasePrice(), result.getBasePrice());
    }

    @Test
    void getPricingConfig_NotFound_ThrowsException() {
        when(pricingConfigRepository.findAll()).thenReturn(Collections.emptyList());

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            pricingService.getPricingConfig();
        });

        assertEquals("Pricing config not set", exception.getMessage());
    }

    @Test
    void calculateTotalAmount_WithinBaseDistance_ReturnsBasePrice() {
        when(pricingConfigRepository.findAll()).thenReturn(Collections.singletonList(pricingConfig));

        double distanceKm = 3.0; // Less than base distance (5.0 km)
        double result = pricingService.calculateTotalAmount(distanceKm);

        assertEquals(100.0, result, 0.001); // Base price only
    }

    @Test
    void calculateTotalAmount_ExceedsBaseDistance_ReturnsCalculatedPrice() {
        when(pricingConfigRepository.findAll()).thenReturn(Collections.singletonList(pricingConfig));

        double distanceKm = 7.0; // Exceeds base distance by 2 km
        double expected = 100.0 + (2.0 * 20.0); // Base price + (additional distance * price per km)
        double result = pricingService.calculateTotalAmount(distanceKm);

        assertEquals(expected, result, 0.001);
    }

    @Test
    void calculateTotalAmount_NoConfig_UsesDefault() {
        when(pricingConfigRepository.findAll()).thenReturn(Collections.emptyList());

        double distanceKm = 7.0; // Exceeds default base distance (5.0 km)
        double expected = 100.0 + (2.0 * 20.0); // Default base price + (additional distance * default price per km)
        double result = pricingService.calculateTotalAmount(distanceKm);

        assertEquals(expected, result, 0.001);
    }
}