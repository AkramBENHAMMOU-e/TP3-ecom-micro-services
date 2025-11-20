package com.tp.customerservice.service.Impl;

import com.tp.customerservice.entities.Customer;
import com.tp.customerservice.repository.CustomerRepository;
import com.tp.customerservice.service.CustomerService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerServiceImp implements CustomerService {

    private final CustomerRepository customerRepository;
    public CustomerServiceImp(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }


    public List<Customer> getAllCustomers(){
        return customerRepository.findAll();
    }
    public Customer getCustomerById(Long id){
        return customerRepository.findById(id).get();
    }
    public Customer createCustomer(Customer customer){
        return customerRepository.save(customer);
    }
    public void deleteCustomer(Long id){
        customerRepository.deleteById(id);
    }
    public Customer updateCustomer(Customer customer){
        Customer c = customerRepository.findById(customer.getId()).get();
        c.setName(customer.getName());
        c.setEmail(customer.getEmail());
        return customerRepository.save(c);
    }
    public Customer getCustomerByNameAndEmail(String name, String email){
        return customerRepository.findByNameIgnoreCaseAndEmailIgnoreCase(name, email);
    }
}
