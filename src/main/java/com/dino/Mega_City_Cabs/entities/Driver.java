package com.dino.Mega_City_Cabs.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
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

    @NotBlank(message = "Name is required")
    private String name;

    @NotBlank(message = "NIC number is required")
    @Column(unique = true)
    private String nicNumber;

    @NotBlank(message = "Driving license number is required")
    @Column(unique = true)
    private String drivingLicenseNumber;

    @NotBlank(message = "Contact number is required")
    @Pattern(regexp = "^\\d{10,15}$", message = "Invalid contact number")
    private String contactNumber;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AvailabilityStatus availabilityStatus = AvailabilityStatus.AVAILABLE;

    @OneToOne(mappedBy = "driver", cascade = CascadeType.ALL, orphanRemoval = true)
    private User user;

    @OneToOne
    @JoinColumn(name = "car_id", nullable = true)
    private Car car;

    @OneToMany(mappedBy = "driver", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Booking> bookings;

    @ManyToOne
    @JoinColumn(name = "admin_id", nullable = false)
    private Admin admin;

    public enum AvailabilityStatus {
        AVAILABLE, ON_DUTY
    }
}