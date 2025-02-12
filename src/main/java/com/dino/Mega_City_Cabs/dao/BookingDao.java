package com.dino.Mega_City_Cabs.dao;

import com.dino.Mega_City_Cabs.Model.Booking;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookingDao extends JpaRepository<Booking, Integer> {
}
