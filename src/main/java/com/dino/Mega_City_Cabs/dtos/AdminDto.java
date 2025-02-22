package com.dino.Mega_City_Cabs.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdminDto {
    private Long id;
    private String name;
    private String contactNumber;

    private Long userId;
    private List<Long> carIds;
    private List<Long> driverIds;
    private List<Long> systemLogIds;

}
