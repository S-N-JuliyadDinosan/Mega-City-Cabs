package com.dino.Mega_City_Cabs.repositories;

import com.dino.Mega_City_Cabs.entities.Booking;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findByStatus(Booking.Status status);
    List<Booking> findByCustomerId(Long customerId);
    List<Booking> findByDriverId(Long driverId);
}