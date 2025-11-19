package com.tp.bilingservice.AITools;

import com.tp.bilingservice.entities.Bill;
import com.tp.bilingservice.repository.BillRepository;
import org.springaicommunity.mcp.annotation.McpTool;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class MCPTools {

    private final BillRepository  billRepository;

    public MCPTools(BillRepository billRepository) {
        this.billRepository = billRepository;
    }

   @McpTool(name = "listBills", description = "lister les informations de chaque factures")
    public List<Bill> listBills(){
        return billRepository.findAll();
    }

    @McpTool(name = "getBill", description = "Retourner les informations d'une facture avec son id")
    public Bill getBill(Long id){
        return billRepository.findById(id).get();
    }


}
