package com.dino.Mega_City_Cabs.dao;

import com.dino.Mega_City_Cabs.Model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerDao extends JpaRepository<Customer,Integer> {
}
