package com.tp.inventoryservice.dtos;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter @Setter
public class ProductResponseDto {

    private UUID id;
    private String name;
    private double price;
    private int quantity;
}
