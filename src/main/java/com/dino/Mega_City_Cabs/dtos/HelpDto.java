package com.dino.Mega_City_Cabs.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HelpDto {
    private Long id;
    private String topic;
    private String description;
}