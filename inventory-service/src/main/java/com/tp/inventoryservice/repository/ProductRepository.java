package com.tp.inventoryservice.repository;

import com.tp.inventoryservice.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.UUID;


@RepositoryRestResource
public interface ProductRepository  extends JpaRepository<Product, UUID> {
    void deleteProductByName(String productName);
}
