package com.tp.bilingservice.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.tp.bilingservice.model.Product;
import jakarta.persistence.*;
import lombok.*;

@Entity @AllArgsConstructor @NoArgsConstructor @Getter @Setter @Builder
public class ProductItem {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String productId;
    @ManyToOne
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Bill bill;
    private double unitPrice;
    private int quantity;
    @Transient
    private Product product;


}
