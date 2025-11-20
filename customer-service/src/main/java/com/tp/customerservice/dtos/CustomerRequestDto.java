package com.tp.customerservice.dtos;

import jakarta.validation.constraints.Email;
import lombok.Getter;
import lombok.Setter;

import jakarta.validation.constraints.Size;

@Getter @Setter
public class CustomerRequestDto {
    @Size(min = 3, max = 20 ,message = "Name must be between 3 and 20 characters")
    private String name;
    @Email(message = "Email is not valid")
    private String email;
}
