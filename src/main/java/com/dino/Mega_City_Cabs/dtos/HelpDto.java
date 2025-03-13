package com.dino.Mega_City_Cabs.dtos;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class HelpDto {
    private Long id;
    @NotBlank(message = "Topic is required")
    private String topic;
    @NotBlank(message = "Description is required")
    private String description;
    private String category;
}