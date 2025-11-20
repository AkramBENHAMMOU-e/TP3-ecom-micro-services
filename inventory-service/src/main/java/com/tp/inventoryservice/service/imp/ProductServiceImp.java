package com.tp.inventoryservice.service.imp;

import com.tp.inventoryservice.dtos.ProductRequestDto;
import com.tp.inventoryservice.dtos.ProductResponseDto;
import com.tp.inventoryservice.entities.Product;
import com.tp.inventoryservice.mappers.ProductMapper;
import com.tp.inventoryservice.repository.ProductRepository;
import com.tp.inventoryservice.service.ProductService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ProductServiceImp implements ProductService {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;
    public ProductServiceImp(ProductRepository productRepository, ProductMapper productMapper) {
        this.productRepository = productRepository;
        this.productMapper = productMapper;
    }

    @Override
    public List<ProductResponseDto> getAllProducts() {
        List<Product> products = productRepository.findAll();
        return products.stream().map(product -> productMapper.toDto(product)).toList();
    }

    @Override
    public ProductResponseDto getProductById(UUID id) {
        Product p = productRepository.findById(id).orElse(null);
        return productMapper.toDto(p);
    }

    @Override
    public void deleteProductById(UUID id) {
        productRepository.deleteById(id);
    }

    @Override
    public ProductResponseDto saveProduct(ProductRequestDto product) {
        return productMapper.toDto(productRepository.save(productMapper.toProduct(product)));

    }
    @Override
    public ProductResponseDto updateProduct(UUID id, ProductRequestDto product) {
        Product p = productRepository.findById(id).orElse(null);
        p.setName(product.getName());
        p.setPrice(product.getPrice());
        p.setQuantity(product.getQuantity());
        return productMapper.toDto(productRepository.save(p));
    }
}
