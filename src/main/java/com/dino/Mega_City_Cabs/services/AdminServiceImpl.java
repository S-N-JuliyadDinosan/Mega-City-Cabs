package com.dino.Mega_City_Cabs.services;

import com.dino.Mega_City_Cabs.dtos.AdminDto;
import com.dino.Mega_City_Cabs.entities.Admin;
import com.dino.Mega_City_Cabs.entities.User;
import com.dino.Mega_City_Cabs.repositories.AdminRepository;
import com.dino.Mega_City_Cabs.repositories.UserRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AdminServiceImpl implements AdminService {

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public Admin createAdmin(AdminDto adminDto) {
        try {
            // Validate email uniqueness
            if (userRepository.existsByEmail(adminDto.getEmail())) {
                throw new IllegalArgumentException("Email already exists: " + adminDto.getEmail());
            }

            // Additional validation
            if (adminDto.getName() == null || adminDto.getName().trim().isEmpty()) {
                throw new IllegalArgumentException("Name cannot be empty");
            }
            if (adminDto.getContactNumber() == null || !adminDto.getContactNumber().matches("^\\d{10,15}$")) {
                throw new IllegalArgumentException("Invalid contact number format");
            }

            // Create User
            User user = User.createUser(
                    adminDto.getEmail(),
                    passwordEncoder.encode(adminDto.getPassword()),
                    "ADMIN",
                    new Admin()
            );

            // Set Admin details
            Admin admin = user.getAdmin();
            admin.setName(adminDto.getName());
            admin.setContactNumber(adminDto.getContactNumber());

            // Save User (cascades to Admin)
            userRepository.save(user);
            return admin;
        } catch (IllegalArgumentException e) {
            throw e; // Re-throw for controller to catch
        } catch (Exception e) {
            throw new RuntimeException("Failed to create admin: " + e.getMessage(), e);
        }
    }

    @Override
    @Transactional
    public Admin updateAdmin(AdminDto adminDto) {
        try {
            // Validate ID
            if (adminDto.getId() == null) {
                throw new IllegalArgumentException("Admin ID cannot be null for update");
            }

            // Find existing Admin and User
            Admin admin = adminRepository.findById(adminDto.getId())
                    .orElseThrow(() -> new IllegalArgumentException("Admin not found with ID: " + adminDto.getId()));
            User user = admin.getUser();

            // Validate email uniqueness (if changed)
            if (!user.getEmail().equals(adminDto.getEmail()) && userRepository.existsByEmail(adminDto.getEmail())) {
                throw new IllegalArgumentException("Email already exists: " + adminDto.getEmail());
            }

            // Additional validation
            if (adminDto.getName() == null || adminDto.getName().trim().isEmpty()) {
                throw new IllegalArgumentException("Name cannot be empty");
            }
            if (adminDto.getContactNumber() == null || !adminDto.getContactNumber().matches("^\\d{10,15}$")) {
                throw new IllegalArgumentException("Invalid contact number format");
            }

            // Update fields
            user.setEmail(adminDto.getEmail());
            if (adminDto.getPassword() != null && !adminDto.getPassword().trim().isEmpty()) {
                user.setPassword(passwordEncoder.encode(adminDto.getPassword()));
            }
            admin.setName(adminDto.getName());
            admin.setContactNumber(adminDto.getContactNumber());

            // Save User (cascades to Admin)
            userRepository.save(user);
            return admin;
        } catch (IllegalArgumentException e) {
            throw e; // Re-throw for controller
        } catch (Exception e) {
            throw new RuntimeException("Failed to update admin: " + e.getMessage(), e);
        }
    }

    @Override
    @Transactional
    public void deleteAdmin(Long id) {
        try {
            if (id == null || id <= 0) {
                throw new IllegalArgumentException("Invalid admin ID: " + id);
            }

            Admin admin = adminRepository.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("Admin not found with ID: " + id));

            // Prevent deleting the last admin
            if (adminRepository.count() <= 1) {
                throw new IllegalArgumentException("Cannot delete the last admin in the system");
            }

            userRepository.delete(admin.getUser()); // Deletes User and cascades to Admin
        } catch (IllegalArgumentException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Failed to delete admin: " + e.getMessage(), e);
        }
    }

    @Override
    public Admin getAdminById(Long id) {
        try {
            if (id == null || id <= 0) {
                throw new IllegalArgumentException("Invalid admin ID: " + id);
            }

            return adminRepository.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("Admin not found with ID: " + id));
        } catch (IllegalArgumentException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Failed to retrieve admin: " + e.getMessage(), e);
        }
    }

    @Override
    public Page<Admin> getAllAdmins(int page, int size) {
        try {
            if (page < 0) {
                throw new IllegalArgumentException("Page number cannot be negative");
            }
            if (size <= 0) {
                throw new IllegalArgumentException("Page size must be greater than zero");
            }

            Pageable pageable = PageRequest.of(page, size);
            Page<Admin> admins = adminRepository.findAll(pageable);
            if (admins.isEmpty() && page > 0) {
                throw new IllegalArgumentException("No admins found for page " + page);
            }
            return admins;
        } catch (IllegalArgumentException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Failed to retrieve admins: " + e.getMessage(), e);
        }
    }

    @PostConstruct
    @Transactional
    public void initAdminUser() {
        try {
            if (!userRepository.existsByEmail("admin@megacitycab.com")) {
                User user = User.createUser(
                        "admin@megacitycab.com",
                        passwordEncoder.encode("admin123"),
                        "ADMIN",
                        new Admin()
                );
                Admin admin = user.getAdmin();
                admin.setName("Initial Admin");
                admin.setContactNumber("1234567890");
                userRepository.save(user);
                System.out.println("Initial admin created: admin@megacitycab.com / admin123");
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to initialize admin user: " + e.getMessage(), e);
        }
    }
}