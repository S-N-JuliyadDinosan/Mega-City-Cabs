package com.dino.Mega_City_Cabs.dao;

import com.dino.Mega_City_Cabs.models.Billing;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BillingDao extends JpaRepository<Billing,Long> {
}
