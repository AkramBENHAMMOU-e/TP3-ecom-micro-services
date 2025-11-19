package com.tp.customerservice.repository;

import com.tp.customerservice.entities.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface CustomerRepository extends JpaRepository<Customer, Long> {
    Customer findByNameIgnoreCaseAndEmailIgnoreCase(String name, String email);

}
