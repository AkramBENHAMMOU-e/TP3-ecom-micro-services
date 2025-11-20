package com.tp.customerservice.service;

import com.tp.customerservice.entities.Customer;

import java.util.List;

public interface CustomerService {
    public List<Customer> getAllCustomers();
    public Customer getCustomerById(Long id);
    public Customer createCustomer(Customer customer);
    public void deleteCustomer(Long id);
    public Customer updateCustomer(Customer customer);
    public Customer getCustomerByNameAndEmail(String name, String email);
}
