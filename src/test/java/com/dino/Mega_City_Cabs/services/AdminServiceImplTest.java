package com.dino.Mega_City_Cabs.services;

import com.dino.Mega_City_Cabs.dtos.AdminDto;
import com.dino.Mega_City_Cabs.entities.Admin;
import com.dino.Mega_City_Cabs.entities.User;
import com.dino.Mega_City_Cabs.repositories.AdminRepository;
import com.dino.Mega_City_Cabs.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AdminServiceImplTest {

    @Mock
    private AdminRepository adminRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private AdminServiceImpl adminService;

    private AdminDto adminDto;
    private Admin admin;
    private User user;

    @BeforeEach
    void setUp() {
        adminDto = new AdminDto();
        adminDto.setEmail("test@admin.com");
        adminDto.setPassword("password123");
        adminDto.setName("Test Admin");
        adminDto.setContactNumber("1234567890");

        admin = new Admin();
        admin.setId(1L);
        admin.setName("Test Admin");
        admin.setContactNumber("1234567890");

        user = new User();
        user.setId(1L);
        user.setEmail("test@admin.com");
        user.setPassword("encodedPassword");
        user.setRole("ADMIN");
        user.setAdmin(admin);
        admin.setUser(user);
    }

    @Test
    void createAdmin_Success() {
        when(userRepository.existsByEmail(adminDto.getEmail())).thenReturn(false);
        when(passwordEncoder.encode(adminDto.getPassword())).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(user);

        Admin result = adminService.createAdmin(adminDto);

        assertNotNull(result);
        assertEquals(adminDto.getName(), result.getName());
        assertEquals(adminDto.getContactNumber(), result.getContactNumber());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void createAdmin_EmailExists_ThrowsException() {
        when(userRepository.existsByEmail(adminDto.getEmail())).thenReturn(true);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            adminService.createAdmin(adminDto);
        });

        assertEquals("Email already exists: " + adminDto.getEmail(), exception.getMessage());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void createAdmin_InvalidContactNumber_ThrowsException() {
        adminDto.setContactNumber("invalid");

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            adminService.createAdmin(adminDto);
        });

        assertEquals("Invalid contact number format", exception.getMessage());
    }

    @Test
    void updateAdmin_AdminNotFound_ThrowsException() {
        adminDto.setId(1L);
        when(adminRepository.findById(1L)).thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            adminService.updateAdmin(adminDto);
        });

        assertEquals("Admin not found with ID: 1", exception.getMessage());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void deleteAdmin_Success() {
        when(adminRepository.findById(1L)).thenReturn(Optional.of(admin));
        when(adminRepository.count()).thenReturn(2L);

        adminService.deleteAdmin(1L);

        verify(userRepository, times(1)).delete(user);
    }

    @Test
    void deleteAdmin_LastAdmin_ThrowsException() {
        when(adminRepository.findById(1L)).thenReturn(Optional.of(admin));
        when(adminRepository.count()).thenReturn(1L);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            adminService.deleteAdmin(1L);
        });

        assertEquals("Cannot delete the last admin in the system", exception.getMessage());
        verify(userRepository, never()).delete(any(User.class));
    }

    @Test
    void getAdminById_Success() {
        when(adminRepository.findById(1L)).thenReturn(Optional.of(admin));

        Admin result = adminService.getAdminById(1L);

        assertNotNull(result);
        assertEquals(admin.getName(), result.getName());
    }

    @Test
    void getAdminById_NotFound_ThrowsException() {
        when(adminRepository.findById(1L)).thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            adminService.getAdminById(1L);
        });

        assertEquals("Admin not found with ID: 1", exception.getMessage());
    }

    @Test
    void getAllAdmins_Success() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Admin> adminPage = new PageImpl<>(Collections.singletonList(admin));
        when(adminRepository.findAll(pageable)).thenReturn(adminPage);

        Page<Admin> result = adminService.getAllAdmins(0, 10);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
    }

    @Test
    void getAllAdmins_EmptyPage_ThrowsException() {
        Pageable pageable = PageRequest.of(1, 10);
        when(adminRepository.findAll(pageable)).thenReturn(Page.empty());

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            adminService.getAllAdmins(1, 10);
        });

        assertEquals("No admins found for page 1", exception.getMessage());
    }

    @Test
    void initAdminUser_Success() {
        when(userRepository.existsByEmail("admin@megacitycab.com")).thenReturn(false);
        when(passwordEncoder.encode("admin123")).thenReturn("encodedAdmin123");
        when(userRepository.save(any(User.class))).thenReturn(user);

        adminService.initAdminUser();

        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void initAdminUser_AlreadyExists_NoAction() {
        when(userRepository.existsByEmail("admin@megacitycab.com")).thenReturn(true);

        adminService.initAdminUser();

        verify(userRepository, never()).save(any(User.class));
    }
}