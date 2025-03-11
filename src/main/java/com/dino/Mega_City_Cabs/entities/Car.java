package com.dino.Mega_City_Cabs.entities;

import com.dino.Mega_City_Cabs.enums.CarStatus;
import com.fasterxml.jackson.annotation.JsonBackReference;
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

    @Column(name = "reg_number", nullable = false)
    private String registrationNumber;

    @Column(nullable = false)
    private String model;

    @Column(nullable = false)
    private String color;

    @Column(nullable = false)
    private String make;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CarStatus status = CarStatus.NOT_ASSIGNED;

    @ManyToOne
    @JoinColumn(name = "admin_id", nullable = false)
    @JsonBackReference // Prevents serialization of Admin from Car
    private Admin admin;

    @OneToOne(mappedBy = "car")
    @JsonBackReference // Prevents serialization of Driver from Car
    private Driver driver;
}