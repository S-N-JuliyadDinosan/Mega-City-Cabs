package com.dino.Mega_City_Cabs.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

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

    @ManyToOne
    @JoinColumn(name = "adminManager_id", nullable = false)
    private AdminManager adminManager;

    @OneToMany(mappedBy = "car", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Booking> bookings;
}
