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
public class Admin extends DateAudit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String contactNumber;

    @OneToOne(mappedBy = "admin")
    private User user;

    @OneToMany(mappedBy = "admin" , cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Car> cars;

    @OneToMany(mappedBy = "admin" , cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Driver> drivers;

    @OneToMany(mappedBy = "admin" , cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SystemLog> systemLogs;

}
