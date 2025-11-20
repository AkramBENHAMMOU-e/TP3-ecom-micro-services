package com.tp.inventoryservice.mappers;

import com.tp.inventoryservice.dtos.ProductRequestDto;
import com.tp.inventoryservice.dtos.ProductResponseDto;
import com.tp.inventoryservice.entities.Product;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    ProductResponseDto toDto(Product product);
    Product toProduct(ProductRequestDto productRequestDto);

}
