package com.tp.inventoryservice.web;

import com.tp.inventoryservice.dtos.ProductRequestDto;
import com.tp.inventoryservice.dtos.ProductResponseDto;
import com.tp.inventoryservice.entities.Product;
import com.tp.inventoryservice.repository.ProductRepository;
import com.tp.inventoryservice.service.ProductService;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/products")
public class InventoryRestController {

    private final ProductService productService;
    public InventoryRestController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping()
    public List<ProductResponseDto> getAllProducts(){
        return productService.getAllProducts();
    }

    @GetMapping("/{id}")
    public ProductResponseDto getProductById(@PathVariable UUID id){
        return productService.getProductById(id);
    }

    @PostMapping()
    public ProductResponseDto addProduct(@Valid @RequestBody ProductRequestDto product){
        return productService.saveProduct(product);
    }

    @PutMapping("/{id}")
    public ProductResponseDto updateProduct(@PathVariable UUID id,@Valid @RequestBody ProductRequestDto product){
       return productService.updateProduct(id,product);
    }

    @DeleteMapping("/{id}")
    public void deleteProduct(@PathVariable UUID id){
        productService.deleteProductById(id);
    }


}
