package com.dino.Mega_City_Cabs.repositories;

import com.dino.Mega_City_Cabs.entities.SystemLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SystemLogRepository extends JpaRepository<SystemLog, Long> {
    List<SystemLog> findByCustomerId(Long customerId);
    List<SystemLog> findByAdminId(Long adminId);
    List<SystemLog> findByDriverId(Long driverId);
    List<SystemLog> findByLogLevel(String logLevel);
}