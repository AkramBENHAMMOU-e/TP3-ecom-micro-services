package com.tp.customerservice.service.Impl;

import com.tp.customerservice.dtos.CustomerRequestDto;
import com.tp.customerservice.dtos.CustomerResponseDto;
import com.tp.customerservice.entities.Customer;
import com.tp.customerservice.mappers.CustomerMapper;
import com.tp.customerservice.repository.CustomerRepository;
import com.tp.customerservice.service.CustomerService;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class CustomerServiceImp implements CustomerService {

    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;
    public CustomerServiceImp(CustomerRepository customerRepository,CustomerMapper customerMapper) {
        this.customerRepository = customerRepository;
        this.customerMapper = customerMapper;
    }


    public List<CustomerResponseDto> getAllCustomers(){
        List<Customer> customers = customerRepository.findAll();
        List<CustomerResponseDto> dtos = customers.stream().map(customer -> customerMapper.fromCustomerToCustomerDto(customer)).collect(Collectors.toList());
        return dtos;
    }
    public CustomerResponseDto getCustomerById(Long id){
        Customer customer = customerRepository.findById(id).orElse(null);
        return customerMapper.fromCustomerToCustomerDto(customer);
    }
    public CustomerResponseDto createCustomer(CustomerRequestDto dto){
        Customer customer = customerMapper.fromCustomerDtoToCustomer(dto);
        return customerMapper.fromCustomerToCustomerDto(customerRepository.save(customer));
    }
    public void deleteCustomer(Long id){
        customerRepository.deleteById(id);
    }
    public CustomerResponseDto updateCustomer(Long id , CustomerRequestDto dto){
        Customer c = customerRepository.findById(id).get();
        c.setName(dto.getName());
        c.setEmail(dto.getEmail());
        Customer savedCustomer = customerRepository.save(c);
        return customerMapper.fromCustomerToCustomerDto(savedCustomer);
    }
    public CustomerResponseDto getCustomerByNameAndEmail(String name, String email){
        Customer customer = customerRepository.findByNameIgnoreCaseAndEmailIgnoreCase(name, email);
        return customerMapper.fromCustomerToCustomerDto(customer);
    }
}
