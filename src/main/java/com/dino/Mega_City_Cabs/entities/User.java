package com.dino.Mega_City_Cabs.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Entity
@Data
@Table(name = "\"user\"")
public class User extends DateAudit implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    @Column(nullable = false, unique = true)
    private String email;

    @NotBlank(message = "Password is required")
    @Column(nullable = false)
    private String password;

    @NotBlank(message = "Role is required")
    private String role; // "CUSTOMER", "DRIVER", "ADMIN"

    @OneToOne(cascade = CascadeType.ALL, optional = true)
    @JoinColumn(name = "admin_id", nullable = true)
    private Admin admin;

    @OneToOne(cascade = CascadeType.ALL, optional = true)
    @JoinColumn(name = "customer_id", nullable = true)
    private Customer customer;

    @OneToOne(cascade = CascadeType.ALL, optional = true)
    @JoinColumn(name = "driver_id", nullable = true)
    private Driver driver;

    // Factory method for creating users
    public static User createUser(String email, String password, String role, Object entity) {
        User user = new User();
        user.setEmail(email);
        user.setPassword(password);
        user.setRole(role);
        if ("ADMIN".equals(role)) user.setAdmin((Admin) entity);
        else if ("CUSTOMER".equals(role)) user.setCustomer((Customer) entity);
        else if ("DRIVER".equals(role)) user.setDriver((Driver) entity);
        return user;
    }

    // UserDetails implementation
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorities = new ArrayList<>();
        if (role != null) {
            authorities.add(new SimpleGrantedAuthority("ROLE_" + role));
        }
        return authorities;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @PrePersist
    @PreUpdate
    public void validateSingleRole() {
        int nonNullCount = 0;
        if (admin != null) nonNullCount++;
        if (customer != null) nonNullCount++;
        if (driver != null) nonNullCount++;
        if (nonNullCount != 1) {
            throw new IllegalStateException("A user must have exactly one role entity (Admin, Customer, or Driver)");
        }
    }
}