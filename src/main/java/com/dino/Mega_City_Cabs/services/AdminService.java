package com.dino.Mega_City_Cabs.services;

import com.dino.Mega_City_Cabs.dtos.AdminDto;
import com.dino.Mega_City_Cabs.entities.Admin;
import org.springframework.data.domain.Page;

public interface AdminService {
    Admin createAdmin(AdminDto adminDto);
    Admin updateAdmin(AdminDto adminDto);
    void deleteAdmin(Long id);
    Admin getAdminById(Long id);
    Page<Admin> getAllAdmins(int page, int size);
}