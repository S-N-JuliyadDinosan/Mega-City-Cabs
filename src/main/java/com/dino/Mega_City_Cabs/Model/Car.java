package com.dino.Mega_City_Cabs.Model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Car extends DateAudit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String regNumber;
    private String model;
    private String make;
    private String yearOfManufacture;
    private String status;   //Available,booked,Maintenance

    @ManyToOne
    @JoinColumn(name = "driver_id", nullable = true)
    private Driver driver;
}
