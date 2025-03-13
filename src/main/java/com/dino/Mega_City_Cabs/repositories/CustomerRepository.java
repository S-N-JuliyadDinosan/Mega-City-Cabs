package com.dino.Mega_City_Cabs.repositories;

import com.dino.Mega_City_Cabs.entities.Customer;
import com.dino.Mega_City_Cabs.utils.ValidationMessages;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
    @Query("SELECT c FROM Customer c WHERE c.user.email = :email")
    Customer findByUserEmail(String email);

    boolean existsByNicNumber(@NotBlank(message = ValidationMessages.INVALID_CREDENTIAL) @Size(min = 9, max = 15, message = ValidationMessages.MISMATCH_INPUT) String nicNumber);
}