package com.tp.bilingservice.feign;

import com.tp.bilingservice.model.Product;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.hateoas.PagedModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.UUID;

@FeignClient(name = "inventory-service")
public interface ProductRestClient {
    @GetMapping("/products/{id}")
    public Product getProductById(@PathVariable UUID id);

    @GetMapping("/products")
    public List<Product> getAllProducts();
}
