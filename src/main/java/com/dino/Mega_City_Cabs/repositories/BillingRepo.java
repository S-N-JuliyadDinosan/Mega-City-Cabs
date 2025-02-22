package com.dino.Mega_City_Cabs.repositories;

import com.dino.Mega_City_Cabs.models.Billing;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BillingRepo extends JpaRepository<Billing,Long> {
}
