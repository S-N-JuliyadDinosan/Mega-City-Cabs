package com.dino.Mega_City_Cabs.repositories;

import com.dino.Mega_City_Cabs.entities.User;
import com.dino.Mega_City_Cabs.utils.ValidationMessages;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);

    boolean existsByEmail(@NotBlank(message = ValidationMessages.EMAIL_NOT_EMPTY) @Email(message = ValidationMessages.INVALID_EMAIL) String email);
}