package com.dino.Mega_City_Cabs.services;

import com.dino.Mega_City_Cabs.dtos.CustomerDto;
import com.dino.Mega_City_Cabs.dtos.CustomerRegisterDto;
import com.dino.Mega_City_Cabs.dtos.CustomerResponseDto;
import com.dino.Mega_City_Cabs.entities.Customer;
import com.dino.Mega_City_Cabs.entities.User;
import com.dino.Mega_City_Cabs.repositories.CustomerRepository;
import com.dino.Mega_City_Cabs.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CustomerServiceImpl implements CustomerService {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    @Transactional(readOnly = true)
    public Page<CustomerResponseDto> getAllCustomers(Pageable pageable) {
        try {
            Page<Customer> customers = customerRepository.findAll(pageable);
            if (customers.isEmpty()) {
                throw new IllegalArgumentException("No customers found");
            }
            return customers.map(this::convertToResponseDto);
        } catch (Exception e) {
            throw new RuntimeException("Failed to retrieve customers: " + e.getMessage(), e);
        }
    }

    @Override
    @Transactional
    public CustomerResponseDto registerCustomer(CustomerRegisterDto registerDto) {
        try {
            if (customerRepository.existsByNicNumber(registerDto.getNicNumber())) {
                throw new IllegalArgumentException("NIC number already exists: " + registerDto.getNicNumber());
            }
            if (userRepository.existsByEmail(registerDto.getEmail())) {
                throw new IllegalArgumentException("Email already exists: " + registerDto.getEmail());
            }

            Customer customer = new Customer();
            customer.setName(registerDto.getName());
            customer.setAddress(registerDto.getAddress());
            customer.setNicNumber(registerDto.getNicNumber());
            customer.setPhoneNumber(registerDto.getPhoneNumber());

            User user = User.createUser(
                    registerDto.getEmail(),
                    passwordEncoder.encode(registerDto.getPassword()),
                    "CUSTOMER",
                    customer
            );
            customer.setUser(user);

            Customer savedCustomer = customerRepository.save(customer);
            return convertToResponseDto(savedCustomer);
        } catch (IllegalArgumentException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Failed to register customer: " + e.getMessage(), e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public CustomerResponseDto getCustomerById(Long id) {
        try {
            Customer customer = customerRepository.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("Customer not found with ID: " + id));
            return convertToResponseDto(customer);
        } catch (IllegalArgumentException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Failed to retrieve customer: " + e.getMessage(), e);
        }
    }

    @Override
    @Transactional
    public CustomerResponseDto updateCustomer(Long id, CustomerDto customerDto) {
        try {
            Customer customer = customerRepository.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("Customer not found with ID: " + id));

            String currentUserEmail = SecurityContextHolder.getContext().getAuthentication().getName();
            System.out.println("Current user email: " + currentUserEmail);
            System.out.println("Customer email: " + (customer.getUser() != null ? customer.getUser().getEmail() : "null"));
            System.out.println("Authorities: " + SecurityContextHolder.getContext().getAuthentication().getAuthorities());

            if (!customer.getUser().getEmail().equals(currentUserEmail) &&
                    !SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream()
                            .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
                throw new SecurityException("Unauthorized to update this customer");
            }

            if (!customer.getNicNumber().equals(customerDto.getNicNumber()) &&
                    customerRepository.existsByNicNumber(customerDto.getNicNumber())) {
                throw new IllegalArgumentException("NIC number already exists: " + customerDto.getNicNumber());
            }

            customer.setName(customerDto.getName());
            customer.setAddress(customerDto.getAddress());
            customer.setNicNumber(customerDto.getNicNumber());
            customer.setPhoneNumber(customerDto.getPhoneNumber());

            Customer updatedCustomer = customerRepository.save(customer);
            return convertToResponseDto(updatedCustomer);
        } catch (IllegalArgumentException | SecurityException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Failed to update customer: " + e.getMessage(), e);
        }
    }

    @Override
    @Transactional
    public void deleteCustomer(Long id) {
        try {
            Customer customer = customerRepository.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("Customer not found with ID: " + id));

            // Authorization check
            String currentUserEmail = SecurityContextHolder.getContext().getAuthentication().getName();
            if (!customer.getUser().getEmail().equals(currentUserEmail) &&
                    !SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream()
                            .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
                throw new SecurityException("Unauthorized to delete this customer");
            }

            customerRepository.delete(customer);
        } catch (IllegalArgumentException | SecurityException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Failed to delete customer: " + e.getMessage(), e);
        }
    }

    private CustomerResponseDto convertToResponseDto(Customer customer) {
        CustomerResponseDto response = new CustomerResponseDto();
        response.setId(customer.getId());
        response.setName(customer.getName());
        response.setAddress(customer.getAddress());
        response.setNicNumber(customer.getNicNumber());
        response.setPhoneNumber(customer.getPhoneNumber());
        response.setUserId(customer.getUser().getId());
        return response;
    }
}