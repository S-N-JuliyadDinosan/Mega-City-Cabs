package com.dino.Mega_City_Cabs.Model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Car {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String regNumber;
    private String model;
    private String make;
    @Lob
    private byte[] image;
    private String yearOfManufacture;
    private String status;   //Available,booked,Maintenance
}
