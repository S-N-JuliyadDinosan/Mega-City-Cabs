package com.dino.Mega_City_Cabs.repositories;

import com.dino.Mega_City_Cabs.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepo extends JpaRepository<User, Long> {
    User findByUserName(String username);
}
