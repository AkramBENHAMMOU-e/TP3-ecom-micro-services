package com.tp.inventoryservice.AITools;

import com.tp.inventoryservice.entities.Product;
import com.tp.inventoryservice.repository.ProductRepository;
import org.springaicommunity.mcp.annotation.McpArg;
import org.springaicommunity.mcp.annotation.McpTool;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class MCPTools {

    private final ProductRepository productRepository;

    public MCPTools(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @McpTool(name = "getAllProducts",description = "Lister les informations de tous les produits")
    public List<Product> getAllProducts(){
        return productRepository.findAll();
    }

    @McpTool(name = "addProduct", description = "ajouter un produit")
    public Product addProduct(@McpArg(name = "produit", description = "le produit à ajouter") Product product){
        return productRepository.save(product);
    }

    @McpTool(name = "updateProduct", description = "modifier les informations d'un produit")
    public Product updateProduct(@McpArg(name = "produit", description = "le produit à mettre à jour") Product product){
        Product p = productRepository.findById(product.getId()).get();
        p.setName(product.getName());
        p.setPrice(product.getPrice());
        p.setQuantity(product.getQuantity());
        return productRepository.save(p);
    }

    @McpTool()
    public void deleteProduct(@McpArg(name = "productName", description = "le nom du produit à supprimer") String productName){
        productRepository.deleteProductByName(productName);
    }


}
