package com.tp.bilingservice.feign;

import com.tp.bilingservice.model.Product;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.hateoas.PagedModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
@FeignClient(name = "inventory-service")
public interface ProductRestClient {
    @GetMapping("/api/products/{id}")
    public Product getProductById(@PathVariable String id);

    @GetMapping("/api/products")
    public PagedModel<Product> getAllProducts();
}
