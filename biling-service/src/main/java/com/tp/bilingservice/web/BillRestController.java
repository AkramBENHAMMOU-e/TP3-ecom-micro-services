package com.tp.bilingservice.web;

import com.tp.bilingservice.entities.Bill;
import com.tp.bilingservice.feign.CustomerRestClient;
import com.tp.bilingservice.feign.ProductRestClient;
import com.tp.bilingservice.repository.BillRepository;
import com.tp.bilingservice.repository.ProductItemRepository;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class BillRestController {

    private BillRepository billRepository;
    private ProductItemRepository productItemRepository;
    private ProductRestClient productRestClient;
    private CustomerRestClient customerRestClient;


    @GetMapping("api/bills/{id}")
    public Bill getBill(@PathVariable Long id){
        Bill bill = billRepository.findById(id).get();
        bill.setCustomer(customerRestClient.getCustomerById(bill.getCustomerId()));
        bill.getProductItems().forEach(productItem -> {
            productItem.setProduct(productRestClient.getProductById(productItem.getProductId()));
        });
        return bill;

    }
}
