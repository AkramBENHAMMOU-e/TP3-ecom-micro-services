package com.tp.inventoryservice;

import com.tp.inventoryservice.entities.Product;
import com.tp.inventoryservice.repository.ProductRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.UUID;

@SpringBootApplication
public class InventoryServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(InventoryServiceApplication.class, args);
	}

	@Bean
	CommandLineRunner commandLineRunner(ProductRepository productRepository){

        return args -> {
        	productRepository.save(Product.builder()
					.name("computer").price(122)
					.quantity(22).build());
			productRepository.save(Product.builder()
					.name("printer").price(333)
					.quantity(19).build());
        };
    }

}
