package com.tp.inventoryservice.AITools;

import com.tp.inventoryservice.entities.Product;
import com.tp.inventoryservice.repository.ProductRepository;
import org.springaicommunity.mcp.annotation.McpArg;
import org.springaicommunity.mcp.annotation.McpTool;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class MCPTools {

    private final ProductRepository productRepository;

    public MCPTools(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @McpTool(name = "getAllProducts", description = "Lister les informations de tous les produits")
    public List<Product> getAllProducts(){
        return productRepository.findAll();
    }

    @McpTool(name = "addProduct", description = "ajouter un produit")
    public Product addProduct(@McpArg(name = "produit", description = "le produit à ajouter") Product product){
        return productRepository.save(product);
    }

    @McpTool(name = "updateProduct", description = "modifier les informations d'un produit")
    public Product updateProduct(@McpArg(name = "produit", description = "le produit à mettre à jour") Product product){
        Product p = productRepository.findById(product.getId()).orElse(null);
        if (p == null) {
            return null;
        }
        p.setName(product.getName());
        p.setPrice(product.getPrice());
        p.setQuantity(product.getQuantity());
        return productRepository.save(p);
    }

    @McpTool(name = "deleteProduct", description = "Supprimer un produit par son nom")
    public Boolean deleteProduct(@McpArg(name = "productName", description = "le nom du produit à supprimer") String productName){
        try {
        productRepository.deleteProductByName(productName);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @McpTool(name = "getProductById", description = "Retourner un produit par son identifiant UUID")
    public Product getProductById(@McpArg(name = "productId", description = "l'identifiant UUID du produit") UUID id){
        return productRepository.findById(id).orElse(null);
    }

    @McpTool(name = "searchProductsByName", description = "Rechercher des produits par nom (recherche partielle, insensible à la casse)")
    public List<Product> searchProductsByName(@McpArg(name = "name", description = "le nom ou une partie du nom à rechercher") String name){
        if (name == null || name.trim().isEmpty()) {
            return Collections.emptyList();
        }
        String searchName = name.toLowerCase();
        return productRepository.findAll().stream()
                .filter(product -> product.getName() != null && 
                        product.getName().toLowerCase().contains(searchName))
                .collect(Collectors.toList());
    }

    @McpTool(name = "getProductsByPriceRange", description = "Retourner les produits dont le prix est dans une fourchette donnée")
    public List<Product> getProductsByPriceRange(
            @McpArg(name = "minPrice", description = "le prix minimum") Double minPrice,
            @McpArg(name = "maxPrice", description = "le prix maximum") Double maxPrice){
        return productRepository.findAll().stream()
                .filter(product -> {
                    double price = product.getPrice();
                    return price >= minPrice && price <= maxPrice;
                })
                .collect(Collectors.toList());
    }

    @McpTool(name = "getProductsWithLowStock", description = "Retourner les produits dont la quantité en stock est inférieure ou égale à un seuil")
    public List<Product> getProductsWithLowStock(@McpArg(name = "threshold", description = "le seuil de quantité minimum") Integer threshold){
        return productRepository.findAll().stream()
                .filter(product -> product.getQuantity() <= threshold)
                .collect(Collectors.toList());
    }

    @McpTool(name = "getOutOfStockProducts", description = "Retourner tous les produits en rupture de stock (quantité = 0)")
    public List<Product> getOutOfStockProducts(){
        return productRepository.findAll().stream()
                .filter(product -> product.getQuantity() == 0)
                .collect(Collectors.toList());
    }

    @McpTool(name = "getInStockProducts", description = "Retourner tous les produits disponibles en stock (quantité > 0)")
    public List<Product> getInStockProducts(){
        return productRepository.findAll().stream()
                .filter(product -> product.getQuantity() > 0)
                .collect(Collectors.toList());
    }

    @McpTool(name = "updateProductQuantity", description = "Mettre à jour la quantité en stock d'un produit")
    public Product updateProductQuantity(
            @McpArg(name = "productId", description = "l'identifiant UUID du produit") UUID id,
            @McpArg(name = "quantity", description = "la nouvelle quantité") Integer quantity){
        Product product = productRepository.findById(id).orElse(null);
        if (product == null) {
            return null;
        }
        product.setQuantity(quantity);
        return productRepository.save(product);
    }

    @McpTool(name = "increaseProductQuantity", description = "Augmenter la quantité en stock d'un produit d'une certaine valeur")
    public Product increaseProductQuantity(
            @McpArg(name = "productId", description = "l'identifiant UUID du produit") UUID id,
            @McpArg(name = "amount", description = "la quantité à ajouter") Integer amount){
        Product product = productRepository.findById(id).orElse(null);
        if (product == null) {
            return null;
        }
        product.setQuantity(product.getQuantity() + amount);
        return productRepository.save(product);
    }

    @McpTool(name = "decreaseProductQuantity", description = "Diminuer la quantité en stock d'un produit d'une certaine valeur")
    public Product decreaseProductQuantity(
            @McpArg(name = "productId", description = "l'identifiant UUID du produit") UUID id,
            @McpArg(name = "amount", description = "la quantité à soustraire") Integer amount){
        Product product = productRepository.findById(id).orElse(null);
        if (product == null) {
            return null;
        }
        int newQuantity = Math.max(0, product.getQuantity() - amount);
        product.setQuantity(newQuantity);
        return productRepository.save(product);
    }

    @McpTool(name = "getInventoryStatistics", description = "Obtenir des statistiques sur l'inventaire : nombre total, valeur totale, produits en rupture, etc.")
    public Map<String, Object> getInventoryStatistics(){
        List<Product> allProducts = productRepository.findAll();
        
        long totalProducts = allProducts.size();
        double totalValue = allProducts.stream()
                .mapToDouble(product -> product.getPrice() * product.getQuantity())
                .sum();
        double averagePrice = allProducts.stream()
                .mapToDouble(Product::getPrice)
                .average()
                .orElse(0.0);
        int totalQuantity = allProducts.stream()
                .mapToInt(Product::getQuantity)
                .sum();
        long outOfStockCount = allProducts.stream()
                .filter(product -> product.getQuantity() == 0)
                .count();
        long lowStockCount = allProducts.stream()
                .filter(product -> product.getQuantity() > 0 && product.getQuantity() <= 10)
                .count();
        
        Product mostExpensive = allProducts.stream()
                .max(Comparator.comparing(Product::getPrice))
                .orElse(null);
        Product leastExpensive = allProducts.stream()
                .min(Comparator.comparing(Product::getPrice))
                .orElse(null);
        
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalProducts", totalProducts);
        stats.put("totalInventoryValue", totalValue);
        stats.put("averagePrice", averagePrice);
        stats.put("totalQuantity", totalQuantity);
        stats.put("outOfStockCount", outOfStockCount);
        stats.put("lowStockCount", lowStockCount);
        if (mostExpensive != null) {
            stats.put("mostExpensiveProduct", mostExpensive.getName() + " (" + mostExpensive.getPrice() + ")");
        }
        if (leastExpensive != null) {
            stats.put("leastExpensiveProduct", leastExpensive.getName() + " (" + leastExpensive.getPrice() + ")");
        }
        
        return stats;
    }

    @McpTool(name = "getProductsByQuantityRange", description = "Retourner les produits dont la quantité est dans une fourchette donnée")
    public List<Product> getProductsByQuantityRange(
            @McpArg(name = "minQuantity", description = "la quantité minimum") Integer minQuantity,
            @McpArg(name = "maxQuantity", description = "la quantité maximum") Integer maxQuantity){
        return productRepository.findAll().stream()
                .filter(product -> {
                    int quantity = product.getQuantity();
                    return quantity >= minQuantity && quantity <= maxQuantity;
                })
                .collect(Collectors.toList());
    }

    @McpTool(name = "getMostExpensiveProducts", description = "Retourner les N produits les plus chers")
    public List<Product> getMostExpensiveProducts(@McpArg(name = "limit", description = "le nombre de produits à retourner") Integer limit){
        return productRepository.findAll().stream()
                .sorted(Comparator.comparing(Product::getPrice).reversed())
                .limit(limit != null ? limit : 10)
                .collect(Collectors.toList());
    }

    @McpTool(name = "getCheapestProducts", description = "Retourner les N produits les moins chers")
    public List<Product> getCheapestProducts(@McpArg(name = "limit", description = "le nombre de produits à retourner") Integer limit){
        return productRepository.findAll().stream()
                .sorted(Comparator.comparing(Product::getPrice))
                .limit(limit != null ? limit : 10)
                .collect(Collectors.toList());
    }

    @McpTool(name = "getProductsWithHighestStock", description = "Retourner les N produits avec le plus grand stock")
    public List<Product> getProductsWithHighestStock(@McpArg(name = "limit", description = "le nombre de produits à retourner") Integer limit){
        return productRepository.findAll().stream()
                .sorted(Comparator.comparing(Product::getQuantity).reversed())
                .limit(limit != null ? limit : 10)
                .collect(Collectors.toList());
    }

    @McpTool(name = "updateProductPrice", description = "Mettre à jour le prix d'un produit")
    public Product updateProductPrice(
            @McpArg(name = "productId", description = "l'identifiant UUID du produit") UUID id,
            @McpArg(name = "price", description = "le nouveau prix") Double price){
        Product product = productRepository.findById(id).orElse(null);
        if (product == null) {
            return null;
        }
        product.setPrice(price);
        return productRepository.save(product);
    }

    @McpTool(name = "deleteProductById", description = "Supprimer un produit par son identifiant UUID")
    public Boolean deleteProductById(@McpArg(name = "productId", description = "l'identifiant UUID du produit à supprimer") UUID id){
        if (productRepository.existsById(id)) {
            productRepository.deleteById(id);
            return true;
        }
        return false;
    }

    @McpTool(name = "getProductsCount", description = "Compter le nombre total de produits")
    public Long getProductsCount(){
        return productRepository.count();
    }

    @McpTool(name = "getTotalInventoryValue", description = "Calculer la valeur totale de l'inventaire (somme de prix × quantité pour tous les produits)")
    public Double getTotalInventoryValue(){
        return productRepository.findAll().stream()
                .mapToDouble(product -> product.getPrice() * product.getQuantity())
                .sum();
    }

    @McpTool(name = "checkProductAvailability", description = "Vérifier si un produit est disponible en quantité suffisante")
    public Boolean checkProductAvailability(
            @McpArg(name = "productId", description = "l'identifiant UUID du produit") UUID id,
            @McpArg(name = "requiredQuantity", description = "la quantité requise") Integer requiredQuantity){
        Product product = productRepository.findById(id).orElse(null);
        return product != null && product.getQuantity() >= requiredQuantity;
    }

}
