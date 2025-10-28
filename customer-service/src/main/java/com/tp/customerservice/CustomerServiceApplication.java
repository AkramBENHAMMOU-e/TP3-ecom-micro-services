package com.tp.customerservice;

import com.tp.customerservice.config.CustomerConfigParams;
import com.tp.customerservice.entities.Customer;
import com.tp.customerservice.repository.CustomerRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EnableConfigurationProperties(CustomerConfigParams.class)
public class CustomerServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(CustomerServiceApplication.class, args);
    }

    @Bean
    CommandLineRunner commandLineRunner(CustomerRepository customerRepository){
        return args -> {
            customerRepository.save(Customer.builder()
                            .name("ahmed").email("ahmed@gmail.com")
                    .build());
            customerRepository.save(Customer.builder()
                    .name("aya").email("aya@gmail.com")
                    .build());
            customerRepository.save(Customer.builder()
                    .name("ali").email("ali@gmail.com")
                    .build());

            customerRepository.findAll().forEach(System.out::println);

        };
    }

}
