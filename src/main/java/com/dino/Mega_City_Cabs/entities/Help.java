package com.dino.Mega_City_Cabs.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "help")
public class Help extends DateAudit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Topic is required")
    @Column(name = "topic", nullable = false)
    private String topic;

    @NotBlank(message = "Description is required")
    @Column(name = "description", nullable = false, columnDefinition = "TEXT")
    private String description;

    @Column(name = "category")
    private String category; // e.g., "Booking", "Billing"
}