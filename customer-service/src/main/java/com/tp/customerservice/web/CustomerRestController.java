package com.tp.customerservice.web;

import com.tp.customerservice.dtos.CustomerRequestDto;
import com.tp.customerservice.dtos.CustomerResponseDto;
import com.tp.customerservice.entities.Customer;
import com.tp.customerservice.service.CustomerService;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api")
public class CustomerRestController {

    private final CustomerService customerService;
    public CustomerRestController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping("/customers")
    public List<CustomerResponseDto> getCustomers(){
        return customerService.getAllCustomers();
    }
    @GetMapping("/customers/{id}")
    public CustomerResponseDto getCustomerById(@PathVariable Long id){
        return customerService.getCustomerById(id);

    }
    @GetMapping("/customer")
    public CustomerResponseDto getCustomerByNameAndEmail(
            @RequestParam String name,
            @RequestParam String email) {

        return customerService.getCustomerByNameAndEmail(name, email);

    }

    @PostMapping("/customers")
    public CustomerResponseDto createCustomer(@Valid @RequestBody CustomerRequestDto customer){
        return customerService.createCustomer(customer);
    }

    @DeleteMapping("/customers/{id}")
    public void deleteCustomer(@PathVariable Long id){
        customerService.deleteCustomer(id);
    }

    @PutMapping("/customers/{id}")
    public CustomerResponseDto updateCustomer(@PathVariable Long id,@Valid @RequestBody CustomerRequestDto customer){
        return customerService.updateCustomer(id,customer);
    }
}
