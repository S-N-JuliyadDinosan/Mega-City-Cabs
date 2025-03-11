package com.dino.Mega_City_Cabs.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
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

    @NotBlank(message = "Registration number is required")
    @Column(unique = true)
    private String regNumber;

    @NotBlank(message = "Model is required")
    private String model;

    @NotBlank(message = "Make is required")
    private String make;

    private String yearOfManufacture;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CarStatus status = CarStatus.AVAILABLE;

    @ManyToOne
    @JoinColumn(name = "driver_id", nullable = true)
    @JsonBackReference
    private Driver driver;

    @ManyToOne
    @JoinColumn(name = "admin_id", nullable = false)
    @JsonBackReference
    private Admin admin;

    @OneToMany(mappedBy = "car", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Booking> bookings;

    public enum CarStatus {
        NOT_ASSIGNED, AVAILABLE, BOOKED, MAINTENANCE
    }
}