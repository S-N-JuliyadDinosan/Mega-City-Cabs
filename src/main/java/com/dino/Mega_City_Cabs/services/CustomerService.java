package com.dino.Mega_City_Cabs.services;

import com.dino.Mega_City_Cabs.dtos.CustomerDto;
import com.dino.Mega_City_Cabs.dtos.CustomerRegisterDto;
import com.dino.Mega_City_Cabs.dtos.CustomerResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CustomerService {
    Page<CustomerResponseDto> getAllCustomers(Pageable pageable);
    CustomerResponseDto registerCustomer(CustomerRegisterDto registerDto);
    CustomerResponseDto getCustomerById(Long id);
    CustomerResponseDto updateCustomer(Long id, CustomerDto customerDto);
    void deleteCustomer(Long id);
}