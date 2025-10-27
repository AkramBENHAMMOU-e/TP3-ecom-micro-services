package com.tp.bilingservice.entities;

import com.tp.bilingservice.model.Product;
import jakarta.persistence.*;
import lombok.*;

@Entity @AllArgsConstructor @NoArgsConstructor @Getter @Setter @Builder
public class ProductItem {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String productId;
    @ManyToOne
    private Bill bill;
    private double unitPrice;
    private int quantity;
    @Transient
    private Product product;


}
