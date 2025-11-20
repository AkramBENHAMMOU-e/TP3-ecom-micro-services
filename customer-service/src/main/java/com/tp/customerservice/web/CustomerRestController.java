package com.tp.customerservice.web;

import com.tp.customerservice.entities.Customer;
import com.tp.customerservice.service.CustomerService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class CustomerRestController {

    private final CustomerService customerService;
    public CustomerRestController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping("/customers")
    public List<Customer> getCustomers(){
        return customerService.getAllCustomers();
    }
    @GetMapping("/customers/{id}")
    public Customer getCustomerById(@PathVariable Long id){
        return customerService.getCustomerById(id);

    }
    @GetMapping("/customer")
    public Customer getCustomerByNameAndEmail(
            @RequestParam String name,
            @RequestParam String email) {

        return customerService.getCustomerByNameAndEmail(name, email);

    }

    @PostMapping("/customers")
    public Customer createCustomer(@RequestBody Customer customer){
        return customerService.createCustomer(customer);
    }

    @DeleteMapping("/customers/{id}")
    public void deleteCustomer(@PathVariable Long id){
        customerService.deleteCustomer(id);
    }

    @PutMapping("/customers/{id}")
    public Customer updateCustomer(@PathVariable Long id,@RequestBody Customer customer){
        customer.setId(id);
        return customerService.updateCustomer(customer);
    }
}
