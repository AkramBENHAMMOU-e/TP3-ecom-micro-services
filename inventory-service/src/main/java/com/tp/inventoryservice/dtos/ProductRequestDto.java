package com.tp.inventoryservice.dtos;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductRequestDto {
    @NotBlank(message = "Le nom du produit est obligatoire")
    @Size(min = 2, max = 100, message = "Le nom doit contenir entre 2 et 100 caractères")
    private String name ;
    @Positive(message = "Le prix doit être supérieur à 0")
    private double price ;
    @Min(value = 0, message = "La quantité ne peut pas être négative")
    private int quantity ;
}
