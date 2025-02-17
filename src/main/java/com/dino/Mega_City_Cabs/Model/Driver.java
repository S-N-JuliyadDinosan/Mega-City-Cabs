package com.dino.Mega_City_Cabs.Model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor

public class Driver extends DateAudit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String nicNumber;
    private String drivingLicenseNumber;
    private String contactNumber;
    private String availabilityStatus;  //Available,On duty

    @OneToOne   //For user credentials
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @OneToMany(mappedBy = "driver",cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Car> car;

    @OneToMany(mappedBy = "driver", cascade = CascadeType.ALL,orphanRemoval = true)
    private List<Booking> bookings;

    @ManyToOne
    @JoinColumn(name = "adminManager_id", nullable = false)
    private AdminManager adminManager;

}
