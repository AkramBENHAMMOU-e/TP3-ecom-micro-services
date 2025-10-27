package com.tp.bilingservice.feign;

import com.tp.bilingservice.model.Customer;
import jakarta.ws.rs.Path;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.data.repository.query.Param;
import org.springframework.hateoas.PagedModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "customer-service")

public interface CustomerRestClient {
    @GetMapping("/customers/{id}")
    public Customer getCustomerById(@PathVariable Long id);

    @GetMapping("/customers")
    public PagedModel<Customer> getAllCustomers();

}
