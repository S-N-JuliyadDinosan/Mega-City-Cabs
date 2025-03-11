package com.dino.Mega_City_Cabs.repositories;

import com.dino.Mega_City_Cabs.entities.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
}
