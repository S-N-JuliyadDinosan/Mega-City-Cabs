package com.dino.Mega_City_Cabs.controllers;

import com.dino.Mega_City_Cabs.dtos.PricingConfigDto;
import com.dino.Mega_City_Cabs.enums.RestApiResponseStatusCodes;
import com.dino.Mega_City_Cabs.services.PricingService;
import com.dino.Mega_City_Cabs.utils.EndpointBundle;
import com.dino.Mega_City_Cabs.utils.ResponseWrapper;
import com.dino.Mega_City_Cabs.utils.ValidationMessages;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(EndpointBundle.PRICING)
public class PricingController {

    @Autowired
    private PricingService pricingService;

    @PostMapping
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<ResponseWrapper<PricingConfigDto>> setPricingConfig(
            @Valid @RequestBody PricingConfigDto pricingConfigDto) {
        try {
            PricingConfigDto config = pricingService.setPricingConfig(pricingConfigDto);
            return ResponseEntity.ok(new ResponseWrapper<>(
                    RestApiResponseStatusCodes.SUCCESS.getCode(),
                    "Pricing configured successfully",
                    config
            ));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseWrapper<>(
                    RestApiResponseStatusCodes.BAD_REQUEST.getCode(),
                    e.getMessage(),
                    null
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseWrapper<>(
                    RestApiResponseStatusCodes.INTERNAL_SERVER_ERROR.getCode(),
                    ValidationMessages.SAVE_FAILED,
                    null
            ));
        }
    }

    @GetMapping
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<ResponseWrapper<PricingConfigDto>> getPricingConfig() {
        try {
            PricingConfigDto config = pricingService.getPricingConfig();
            return ResponseEntity.ok(new ResponseWrapper<>(
                    RestApiResponseStatusCodes.SUCCESS.getCode(),
                    ValidationMessages.RETRIEVED,
                    config
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseWrapper<>(
                    RestApiResponseStatusCodes.INTERNAL_SERVER_ERROR.getCode(),
                    ValidationMessages.RETRIEVED_FAILED,
                    null
            ));
        }
    }
}