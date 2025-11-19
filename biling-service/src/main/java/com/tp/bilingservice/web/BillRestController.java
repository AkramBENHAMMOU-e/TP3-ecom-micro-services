package com.tp.bilingservice.web;

import com.tp.bilingservice.entities.Bill;
import com.tp.bilingservice.feign.CustomerRestClient;
import com.tp.bilingservice.feign.ProductRestClient;
import com.tp.bilingservice.repository.BillRepository;
import com.tp.bilingservice.repository.ProductItemRepository;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api")
public class BillRestController {

    private BillRepository billRepository;
    private ProductItemRepository productItemRepository;
    private ProductRestClient productRestClient;
    private CustomerRestClient customerRestClient;

    @GetMapping("/bills")
    public List<Bill> getBills(){
        return billRepository.findAll();
    }

    @GetMapping("/bills/{id}")
    public Bill getBill(@PathVariable Long id){
        Bill bill = billRepository.findById(id).get();
        bill.setCustomer(customerRestClient.getCustomerById(bill.getCustomerId()));
        bill.getProductItems().forEach(productItem -> {
            productItem.setProduct(productRestClient.getProductById(productItem.getProductId()));
        });
        return bill;

    }

    @PostMapping("/bills")
    public Bill createBill(@RequestBody Bill bill){
        return billRepository.save(bill);
    }

    @DeleteMapping("/bills/{id}")
    public void deleteBill(@PathVariable Long id){
        billRepository.deleteById(id);
    }
}
