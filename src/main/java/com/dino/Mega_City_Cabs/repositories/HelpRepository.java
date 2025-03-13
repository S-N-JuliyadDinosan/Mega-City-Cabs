package com.dino.Mega_City_Cabs.repositories;

import com.dino.Mega_City_Cabs.entities.Help;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HelpRepository extends JpaRepository<Help, Long> {
    List<Help> findByCategory(String category);
}