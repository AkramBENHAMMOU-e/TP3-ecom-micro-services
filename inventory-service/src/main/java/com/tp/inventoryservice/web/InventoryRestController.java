package com.tp.inventoryservice.web;

import com.tp.inventoryservice.entities.Product;
import com.tp.inventoryservice.repository.ProductRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/products")
public class InventoryRestController {

    private ProductRepository productRepository;
    public InventoryRestController(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @GetMapping()
    public List<Product> getAllProducts(){
        return productRepository.findAll();
    }

    @GetMapping("/{id}")
    public Product getProductById(@PathVariable UUID id){
        return productRepository.findById(id).get();
    }

    @PostMapping()
    public Product addProduct(@RequestBody Product product){
        return productRepository.save(product);
    }

    @PutMapping("/{id}")
    public Product updateProduct(@PathVariable UUID id, @RequestBody Product product){
        Product p = productRepository.findById(id).get();
        p.setName(product.getName());
        p.setPrice(product.getPrice());
        p.setName(product.getName());
        p.setQuantity(product.getQuantity());
        return productRepository.save(p);
    }

    @DeleteMapping("/{id}")
    public void deleteProduct(@PathVariable UUID id){
        productRepository.deleteById(id);
    }



}
