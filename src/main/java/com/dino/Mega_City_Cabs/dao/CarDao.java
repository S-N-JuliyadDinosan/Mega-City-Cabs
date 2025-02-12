package com.dino.Mega_City_Cabs.dao;

import com.dino.Mega_City_Cabs.Model.Car;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CarDao extends JpaRepository<Car, Integer> {
}
