package com.dino.Mega_City_Cabs.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "system_logs")
public class SystemLog extends DateAudit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Action performed is required")
    @Column(name = "action_performed", nullable = false)
    private String actionPerformed; // e.g., "LOGIN", "ADD_BOOKING"

    @Column(name = "timestamp", nullable = false)
    private LocalDateTime timeStamp;

    @Column(name = "log_level")
    private String logLevel; // e.g., "INFO", "ERROR"

    @ManyToOne
    @JoinColumn(name = "customer_id", nullable = true)
    private Customer customer;

    @ManyToOne
    @JoinColumn(name = "admin_id", nullable = true)
    @JsonIgnore
    private Admin admin;

    @ManyToOne
    @JoinColumn(name = "driver_id", nullable = true)
    private Driver driver;
}