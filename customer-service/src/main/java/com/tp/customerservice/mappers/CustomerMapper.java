package com.tp.customerservice.mappers;

import com.tp.customerservice.dtos.CustomerRequestDto;
import com.tp.customerservice.dtos.CustomerResponseDto;
import com.tp.customerservice.entities.Customer;
import org.springframework.stereotype.Component;

@Component
public class CustomerMapper {
    public CustomerResponseDto fromCustomerToCustomerDto(Customer customer){
        CustomerResponseDto dto = new CustomerResponseDto();
        dto.setId(customer.getId());
        dto.setName(customer.getName());
        dto.setEmail(customer.getEmail());
        return dto;
    }

    public Customer fromCustomerDtoToCustomer(CustomerRequestDto dto){
        Customer customer = new Customer();
        customer.setName(dto.getName());
        customer.setEmail(dto.getEmail());
        return customer;
    }
}
