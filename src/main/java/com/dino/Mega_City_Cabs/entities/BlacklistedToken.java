package com.dino.Mega_City_Cabs.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

@Data
@Entity
public class BlacklistedToken {
    @Id
    private String token;
}