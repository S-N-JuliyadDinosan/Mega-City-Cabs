package com.dino.Mega_City_Cabs.repositories;

import com.dino.Mega_City_Cabs.models.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminRepo extends JpaRepository<Admin, Long> {
}
