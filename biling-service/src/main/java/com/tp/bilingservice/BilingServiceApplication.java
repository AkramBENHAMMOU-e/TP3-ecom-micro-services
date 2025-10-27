package com.tp.bilingservice;

import com.tp.bilingservice.entities.Bill;
import com.tp.bilingservice.entities.ProductItem;
import com.tp.bilingservice.feign.CustomerRestClient;
import com.tp.bilingservice.feign.ProductRestClient;
import com.tp.bilingservice.model.Customer;
import com.tp.bilingservice.model.Product;
import com.tp.bilingservice.repository.BillRepository;
import com.tp.bilingservice.repository.ProductItemRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Random;

@SpringBootApplication
@EnableFeignClients
public class BilingServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(BilingServiceApplication.class, args);
    }
    @Bean
  CommandLineRunner start(BillRepository billRepository, ProductItemRepository productItemRepository,
                          CustomerRestClient customerRestClient, ProductRestClient productRestClient){

        return args -> {


            Collection<Customer> customers = customerRestClient.getAllCustomers().getContent();
            Collection<Product> products = productRestClient.getAllProducts().getContent();
            customers.forEach(customer -> {
                Bill bill = Bill.builder()
                        .customerId(customer.getId())
                        .billingDate(new Date())
                        .build();
                billRepository.save(bill);
                products.forEach(product -> {
                    ProductItem productItem = ProductItem.builder()
                            .productId(product.getId())
                            .bill(bill)
                            .quantity(1+new Random().nextInt(10))
                            .unitPrice(product.getPrice())
                            .build();
                    productItemRepository.save(productItem);
                });
            });
        };


}
}
