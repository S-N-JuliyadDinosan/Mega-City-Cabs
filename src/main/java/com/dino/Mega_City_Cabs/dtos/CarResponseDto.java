package com.dino.Mega_City_Cabs.dtos;

import lombok.Data;

@Data
public class CarResponseDto {
    private Long id;
    private String registrationNumber;
    private String model;
    private String color;
    private String make;
    private String status;
    private Long adminId;
}