package com.dino.Mega_City_Cabs.dao;

import com.dino.Mega_City_Cabs.models.Help;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HelpDao extends JpaRepository<Help, Long> {
}
