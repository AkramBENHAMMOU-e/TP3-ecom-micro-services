package com.tp.customerservice.service;

import com.tp.customerservice.dtos.CustomerRequestDto;
import com.tp.customerservice.dtos.CustomerResponseDto;
import com.tp.customerservice.entities.Customer;

import java.util.List;

public interface CustomerService {
    public List<CustomerResponseDto> getAllCustomers();
    public CustomerResponseDto getCustomerById(Long id);
    public CustomerResponseDto createCustomer(CustomerRequestDto customerRequestDto);
    public void deleteCustomer(Long id);
    public CustomerResponseDto updateCustomer( Long id , CustomerRequestDto customerRequestDto);
    public CustomerResponseDto getCustomerByNameAndEmail(String name, String email);
}
