package com.tp.customerservice.AITools;

import com.tp.customerservice.entities.Customer;
import com.tp.customerservice.repository.CustomerRepository;
import org.springaicommunity.mcp.annotation.McpArg;
import org.springaicommunity.mcp.annotation.McpTool;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

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
    public Customer getCustomerById(@McpArg(name = "clientID", description = "l'identifiant d'un client") Long id){
        return customerRepository.findById(id).orElse(null);
    }

    @McpTool(name = "addCustomer", description = "Ajouter un client")
    public Customer createCustomer(
            @McpArg(name = "name", description = "le nom d'un client") String name, 
            @McpArg(name = "email", description = "l'email d'un client") String email){
        return customerRepository.save(Customer.builder()
                .name(name)
                .email(email)
                .build());
    }

    @McpTool(name = "updateClient", description = "modifier un client par son nom et son email")
    public Customer updateCustomer(
            @McpArg(name = "clientName", description = "le nom d'un client") String name,
            @McpArg(name = "email", description = "l'email d'un client") String email){
        Customer c = customerRepository.findByNameIgnoreCaseAndEmailIgnoreCase(name, email);
        if (c != null) {
            c.setName(name);
            c.setEmail(email);
            return customerRepository.save(c);
        }
        return null;
    }

    @McpTool(name = "getCustomerByEmail", description = "Rechercher un client par son adresse email")
    public Customer getCustomerByEmail(@McpArg(name = "email", description = "l'adresse email du client") String email){
        return customerRepository.findAll().stream()
                .filter(customer -> customer.getEmail() != null && 
                        customer.getEmail().equalsIgnoreCase(email))
                .findFirst()
                .orElse(null);
    }

    @McpTool(name = "searchCustomersByName", description = "Rechercher des clients par nom (recherche partielle, insensible à la casse)")
    public List<Customer> searchCustomersByName(@McpArg(name = "name", description = "le nom ou une partie du nom à rechercher") String name){
        if (name == null || name.trim().isEmpty()) {
            return Collections.emptyList();
        }
        String searchName = name.toLowerCase();
        return customerRepository.findAll().stream()
                .filter(customer -> customer.getName() != null && 
                        customer.getName().toLowerCase().contains(searchName))
                .collect(Collectors.toList());
    }

    @McpTool(name = "searchCustomersByEmailDomain", description = "Rechercher tous les clients ayant une adresse email d'un domaine spécifique")
    public List<Customer> searchCustomersByEmailDomain(@McpArg(name = "domain", description = "le domaine email (ex: gmail.com)") String domain){
        if (domain == null || domain.trim().isEmpty()) {
            return Collections.emptyList();
        }
        String searchDomain = domain.toLowerCase();
        return customerRepository.findAll().stream()
                .filter(customer -> customer.getEmail() != null && 
                        customer.getEmail().toLowerCase().endsWith("@" + searchDomain))
                .collect(Collectors.toList());
    }

    @McpTool(name = "getCustomerStatistics", description = "Obtenir des statistiques sur les clients : nombre total, domaines email les plus utilisés")
    public Map<String, Object> getCustomerStatistics(){
        List<Customer> allCustomers = customerRepository.findAll();
        
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalCustomers", allCustomers.size());
        
        // Compter les domaines email
        Map<String, Long> domainCount = allCustomers.stream()
                .filter(customer -> customer.getEmail() != null && customer.getEmail().contains("@"))
                .map(customer -> {
                    String email = customer.getEmail();
                    return email.substring(email.indexOf("@") + 1).toLowerCase();
                })
                .collect(Collectors.groupingBy(domain -> domain, Collectors.counting()));
        
        // Top 5 domaines
        List<Map<String, Object>> topDomains = domainCount.entrySet().stream()
                .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                .limit(5)
                .map(entry -> {
                    Map<String, Object> domainInfo = new HashMap<>();
                    domainInfo.put("domain", entry.getKey());
                    domainInfo.put("count", entry.getValue());
                    return domainInfo;
                })
                .collect(Collectors.toList());
        
        stats.put("topEmailDomains", topDomains);
        
        return stats;
    }

    @McpTool(name = "updateCustomerById", description = "Mettre à jour un client par son identifiant")
    public Customer updateCustomerById(
            @McpArg(name = "customerId", description = "l'identifiant du client") Long id,
            @McpArg(name = "name", description = "le nouveau nom (optionnel)") String name,
            @McpArg(name = "email", description = "le nouvel email (optionnel)") String email){
        Customer customer = customerRepository.findById(id).orElse(null);
        if (customer == null) {
            return null;
        }
        if (name != null && !name.trim().isEmpty()) {
            customer.setName(name);
        }
        if (email != null && !email.trim().isEmpty()) {
            customer.setEmail(email);
        }
        return customerRepository.save(customer);
    }

    @McpTool(name = "deleteCustomer", description = "Supprimer un client par son identifiant")
    public Boolean deleteCustomer(@McpArg(name = "customerId", description = "l'identifiant du client à supprimer") Long id){
        if (customerRepository.existsById(id)) {
            customerRepository.deleteById(id);
            return true;
        }
        return false;
    }

    @McpTool(name = "checkEmailExists", description = "Vérifier si une adresse email existe déjà dans la base de données")
    public Boolean checkEmailExists(@McpArg(name = "email", description = "l'adresse email à vérifier") String email){
        return customerRepository.findAll().stream()
                .anyMatch(customer -> customer.getEmail() != null && 
                        customer.getEmail().equalsIgnoreCase(email));
    }

    @McpTool(name = "getCustomersCount", description = "Compter le nombre total de clients")
    public Long getCustomersCount(){
        return customerRepository.count();
    }

    @McpTool(name = "getUniqueEmailDomains", description = "Obtenir la liste de tous les domaines email uniques utilisés par les clients")
    public List<String> getUniqueEmailDomains(){
        return customerRepository.findAll().stream()
                .filter(customer -> customer.getEmail() != null && customer.getEmail().contains("@"))
                .map(customer -> {
                    String email = customer.getEmail();
                    return email.substring(email.indexOf("@") + 1).toLowerCase();
                })
                .distinct()
                .sorted()
                .collect(Collectors.toList());
    }

    @McpTool(name = "findCustomersWithInvalidEmail", description = "Trouver les clients qui n'ont pas d'email ou un email invalide (ne contient pas @)")
    public List<Customer> findCustomersWithInvalidEmail(){
        return customerRepository.findAll().stream()
                .filter(customer -> customer.getEmail() == null || 
                        customer.getEmail().trim().isEmpty() || 
                        !customer.getEmail().contains("@"))
                .collect(Collectors.toList());
    }

    @McpTool(name = "findDuplicatesByEmail", description = "Trouver les clients avec des emails en double")
    public Map<String, List<Customer>> findDuplicatesByEmail(){
        return customerRepository.findAll().stream()
                .filter(customer -> customer.getEmail() != null)
                .collect(Collectors.groupingBy(customer -> customer.getEmail().toLowerCase()))
                .entrySet().stream()
                .filter(entry -> entry.getValue().size() > 1)
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue
                ));
    }

}
