package com.tp.bilingservice.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.tp.bilingservice.model.Product;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity @AllArgsConstructor @NoArgsConstructor @Getter @Setter @Builder
public class ProductItem {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private UUID productId;
    @ManyToOne
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Bill bill;
    private double unitPrice;
    private int quantity;
    @Transient
    private Product product;


}
