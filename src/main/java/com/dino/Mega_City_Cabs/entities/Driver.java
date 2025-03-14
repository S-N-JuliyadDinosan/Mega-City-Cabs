package com.dino.Mega_City_Cabs.entities;

import com.dino.Mega_City_Cabs.enums.AvailabilityStatus;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Driver extends DateAudit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(unique = true)
    private String nicNumber;

    @Column(unique = true)
    private String drivingLicenseNumber;

    private String contactNumber;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AvailabilityStatus availabilityStatus = AvailabilityStatus.AVAILABLE;

    @OneToOne(mappedBy = "driver", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonBackReference // Prevents serialization of User from Driver
    private User user;

    @OneToOne
    @JoinColumn(name = "car_id", nullable = true)
    @JsonManagedReference // Serializes Car from Driver
    private Car car;

    @OneToMany(mappedBy = "driver", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<Booking> bookings;

    @ManyToOne
    @JoinColumn(name = "admin_id", nullable = false)
    @JsonIgnore // Prevents serialization of Admin from Driver
    private Admin admin;
}