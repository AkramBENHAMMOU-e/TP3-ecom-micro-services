package com.tp.inventoryservice.service;

import com.tp.inventoryservice.dtos.ProductRequestDto;
import com.tp.inventoryservice.dtos.ProductResponseDto;

import java.util.List;
import java.util.UUID;

public interface ProductService {

    List<ProductResponseDto> getAllProducts();
    ProductResponseDto getProductById(UUID id);
    void deleteProductById(UUID id);
    ProductResponseDto saveProduct(ProductRequestDto product);
    ProductResponseDto updateProduct(UUID id, ProductRequestDto product);

}
