package com.tp.customerservice.AITools;

import com.tp.customerservice.entities.Customer;
import com.tp.customerservice.repository.CustomerRepository;
import org.springaicommunity.mcp.annotation.McpArg;
import org.springaicommunity.mcp.annotation.McpTool;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class MCPTools {

    private final CustomerRepository customerRepository;
    public MCPTools(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @McpTool(name = "getCustomer", description = "Lister les informations de chaque client")
    public List<Customer> getCustomers(){
        return customerRepository.findAll();
    }

    @McpTool(name = "getCustomerById", description = "Lister les informations d'un client")
    public Customer getCustomerById(@McpArg(name = "clientID",description ="l'identifiant d'un client" ) Long id){
        return customerRepository.findById(id).get();
    }

    @McpTool(name = "addCustomer", description = "Ajouter un client")
    public Customer createCustomer(@McpArg(name = "name",description ="le nom d'un client" ) String name, @McpArg(name = "email",description ="l'email d'un client" ) String email){
        return customerRepository.save(Customer.builder()
                .name(name)
                .email(email)
                .build());
    }

    @McpTool(name = "updateClient", description = "modifier un client par son nom et son email")
    public Customer updateCustomer(@McpArg(name = "clientName",description ="le nom d'un client" ) String name,@McpArg(name = "email",description ="l'email d'un client") String email){
        Customer c = customerRepository.findByNameIgnoreCaseAndEmailIgnoreCase(name,email);
        c.setName(name);
        c.setEmail(email);
        return customerRepository.save(c);
    }
}
