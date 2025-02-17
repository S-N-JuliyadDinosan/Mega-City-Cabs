package com.dino.Mega_City_Cabs.Model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdminManager extends DateAudit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String contactNumber;

    @OneToOne   //For user credentials
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @OneToMany(mappedBy = "adminManager" , cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Car> cars;

    @OneToMany(mappedBy = "adminManager" , cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Driver> drivers;

    @OneToMany(mappedBy = "adminManager" , cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SystemLog> systemLogs;

}
