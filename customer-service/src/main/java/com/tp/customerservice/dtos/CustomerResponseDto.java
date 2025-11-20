package com.tp.customerservice.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class CustomerResponseDto {
    private Long id;
    private String name;
    private String email;

}
