package com.dino.Mega_City_Cabs.services;

import com.dino.Mega_City_Cabs.dtos.CustomerDto;
import com.dino.Mega_City_Cabs.dtos.CustomerRegisterDto;
import com.dino.Mega_City_Cabs.dtos.CustomerResponseDto;
import com.dino.Mega_City_Cabs.entities.Customer;
import com.dino.Mega_City_Cabs.entities.User;
import com.dino.Mega_City_Cabs.repositories.CustomerRepository;
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
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomerServiceImplTest {

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private SecurityContext securityContext;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private CustomerServiceImpl customerService;

    private Customer customer;
    private User user;
    private CustomerRegisterDto registerDto;
    private CustomerDto customerDto;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setEmail("customer@example.com");
        user.setPassword("encodedPassword");
        user.setRole("CUSTOMER");

        customer = new Customer();
        customer.setId(1L);
        customer.setName("John Doe");
        customer.setAddress("123 Main St");
        customer.setNicNumber("NIC123");
        customer.setPhoneNumber("1234567890");
        customer.setUser(user);

        registerDto = new CustomerRegisterDto();
        registerDto.setName("John Doe");
        registerDto.setAddress("123 Main St");
        registerDto.setNicNumber("NIC123");
        registerDto.setPhoneNumber("1234567890");
        registerDto.setEmail("customer@example.com");
        registerDto.setPassword("password123");

        customerDto = new CustomerDto();
        customerDto.setName("Jane Doe");
        customerDto.setAddress("456 Elm St");
        customerDto.setNicNumber("NIC456");
        customerDto.setPhoneNumber("0987654321");

        SecurityContextHolder.setContext(securityContext);
        when(securityContext.getAuthentication()).thenReturn(authentication);
    }



    @Test
    void updateCustomer_Success_AsCustomer() {
        when(authentication.getName()).thenReturn("customer@example.com");
        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
        when(customerRepository.existsByNicNumber("NIC456")).thenReturn(false);
        when(customerRepository.save(customer)).thenReturn(customer);

        CustomerResponseDto result = customerService.updateCustomer(1L, customerDto);

        assertNotNull(result);
        assertEquals(customerDto.getName(), result.getName());
        assertEquals(customerDto.getNicNumber(), result.getNicNumber());
        verify(customerRepository, times(1)).save(customer);
    }



    @Test
    void updateCustomer_Unauthorized_ThrowsException() {
        when(authentication.getName()).thenReturn("other@example.com");
        when(authentication.getAuthorities()).thenReturn(Collections.emptyList());
        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));

        SecurityException exception = assertThrows(SecurityException.class, () -> {
            customerService.updateCustomer(1L, customerDto);
        });

        assertEquals("Unauthorized to update this customer", exception.getMessage());
        verify(customerRepository, never()).save(any(Customer.class));
    }

    @Test
    void updateCustomer_NicExists_ThrowsException() {
        when(authentication.getName()).thenReturn("customer@example.com");
        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
        when(customerRepository.existsByNicNumber("NIC456")).thenReturn(true);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            customerService.updateCustomer(1L, customerDto);
        });

        assertEquals("NIC number already exists: NIC456", exception.getMessage());
        verify(customerRepository, never()).save(any(Customer.class));
    }

    @Test
    void deleteCustomer_Success_AsCustomer() {
        when(authentication.getName()).thenReturn("customer@example.com");
        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));

        customerService.deleteCustomer(1L);

        verify(customerRepository, times(1)).delete(customer);
    }



    @Test
    void deleteCustomer_Unauthorized_ThrowsException() {
        when(authentication.getName()).thenReturn("other@example.com");
        when(authentication.getAuthorities()).thenReturn(Collections.emptyList());
        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));

        SecurityException exception = assertThrows(SecurityException.class, () -> {
            customerService.deleteCustomer(1L);
        });

        assertEquals("Unauthorized to delete this customer", exception.getMessage());
        verify(customerRepository, never()).delete(any(Customer.class));
    }
}