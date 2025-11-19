package com.tp.customerservice.web;

import com.tp.customerservice.entities.Customer;
import com.tp.customerservice.repository.CustomerRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class CustomerRestController {

    private final CustomerRepository customerRepository;
    public CustomerRestController(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @GetMapping("/customers")
    public List<Customer> getCustomers(){
        return customerRepository.findAll();
    }
    @GetMapping("/customers/{id}")
    public Customer getCustomerById(@PathVariable Long id){
        return customerRepository.findById(id).get();

    }

    @GetMapping("/customer")
    public Customer getCustomerByNameAndEmail(
            @RequestParam String name,
            @RequestParam String email) {

        return customerRepository.findByNameIgnoreCaseAndEmailIgnoreCase(name, email);

    }

    @PostMapping("/customers")
    public Customer createCustomer(@RequestBody Customer customer){
        return customerRepository.save(customer);
    }

    @DeleteMapping("/customers/{id}")
    public void deleteCustomer(@PathVariable Long id){
        customerRepository.deleteById(id);
    }

    @PutMapping("/customers/{id}")
    public Customer updateCustomer(@PathVariable Long id,@RequestBody Customer customer){
        Customer c = customerRepository.findById(id).get();
        c.setName(customer.getName());
        c.setEmail(customer.getEmail());
        return customerRepository.save(c);
    }
}
