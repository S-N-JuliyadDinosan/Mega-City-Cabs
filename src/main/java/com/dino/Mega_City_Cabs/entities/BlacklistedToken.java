package com.dino.Mega_City_Cabs.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "blacklisted_tokens")
public class BlacklistedToken {
    @Id
    private String token;
}