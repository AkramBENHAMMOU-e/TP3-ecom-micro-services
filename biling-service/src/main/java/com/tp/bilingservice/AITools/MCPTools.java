package com.tp.bilingservice.AITools;

import com.tp.bilingservice.entities.Bill;
import com.tp.bilingservice.entities.ProductItem;
import com.tp.bilingservice.repository.BillRepository;
import com.tp.bilingservice.repository.ProductItemRepository;
import org.springaicommunity.mcp.annotation.McpArg;
import org.springaicommunity.mcp.annotation.McpTool;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class MCPTools {

    private final BillRepository billRepository;
    private final ProductItemRepository productItemRepository;

    public MCPTools(BillRepository billRepository, ProductItemRepository productItemRepository) {
        this.billRepository = billRepository;
        this.productItemRepository = productItemRepository;
    }

   @McpTool(name = "listBills", description = "lister les informations de chaque factures")
    public List<Bill> listBills(){
        return billRepository.findAll();
    }

    @McpTool(name = "getBill", description = "Retourner les informations d'une facture avec son id")
    public Bill getBill(@McpArg(name = "billId", description = "l'identifiant de la facture") Long id){
        return billRepository.findById(id).orElse(null);
    }

    @McpTool(name = "getBillsByCustomerId", description = "Retourner toutes les factures d'un client spécifique")
    public List<Bill> getBillsByCustomerId(@McpArg(name = "customerId", description = "l'identifiant du client") Long customerId){
        return billRepository.findAll().stream()
                .filter(bill -> bill.getCustomerId().equals(customerId))
                .collect(Collectors.toList());
    }

    @McpTool(name = "calculateBillTotal", description = "Calculer le montant total d'une facture en additionnant tous les produits")
    public Double calculateBillTotal(@McpArg(name = "billId", description = "l'identifiant de la facture") Long billId){
        Bill bill = billRepository.findById(billId).orElse(null);
        if (bill == null) return 0.0;
        
        return bill.getProductItems().stream()
                .mapToDouble(item -> item.getUnitPrice() * item.getQuantity())
                .sum();
    }

    @McpTool(name = "getBillingStatistics", description = "Obtenir des statistiques générales sur les factures : nombre total, montant total, moyenne")
    public Map<String, Object> getBillingStatistics(){
        List<Bill> allBills = billRepository.findAll();
        
        double totalAmount = allBills.stream()
                .mapToDouble(bill -> bill.getProductItems().stream()
                        .mapToDouble(item -> item.getUnitPrice() * item.getQuantity())
                        .sum())
                .sum();
        
        int billCount = allBills.size();
        double averageAmount = billCount > 0 ? totalAmount / billCount : 0.0;
        
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalBills", billCount);
        stats.put("totalAmount", totalAmount);
        stats.put("averageAmount", averageAmount);
        
        return stats;
    }

    @McpTool(name = "getBillsByDateRange", description = "Retourner les factures créées entre deux dates")
    public List<Bill> getBillsByDateRange(
            @McpArg(name = "startDate", description = "date de début (format: yyyy-MM-dd)") String startDateStr,
            @McpArg(name = "endDate", description = "date de fin (format: yyyy-MM-dd)") String endDateStr){
        try {
            java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd");
            Date startDate = sdf.parse(startDateStr);
            Date endDate = sdf.parse(endDateStr);
            
            // Ajouter 1 jour à endDate pour inclure toute la journée
            Calendar cal = Calendar.getInstance();
            cal.setTime(endDate);
            cal.add(Calendar.DAY_OF_MONTH, 1);
            Date endDateInclusive = cal.getTime();
            
            return billRepository.findAll().stream()
                    .filter(bill -> bill.getBillingDate() != null)
                    .filter(bill -> {
                        Date billDate = bill.getBillingDate();
                        return !billDate.before(startDate) && billDate.before(endDateInclusive);
                    })
                    .collect(Collectors.toList());
        } catch (Exception e) {
            return Collections.emptyList();
        }
    }

    @McpTool(name = "getRecentBills", description = "Retourner les N factures les plus récentes")
    public List<Bill> getRecentBills(@McpArg(name = "limit", description = "le nombre maximum de factures à retourner") Integer limit){
        return billRepository.findAll().stream()
                .filter(bill -> bill.getBillingDate() != null)
                .sorted((b1, b2) -> b2.getBillingDate().compareTo(b1.getBillingDate()))
                .limit(limit != null ? limit : 10)
                .collect(Collectors.toList());
    }

    @McpTool(name = "getCustomerTotalBilling", description = "Calculer le montant total facturé pour un client (somme de toutes ses factures)")
    public Double getCustomerTotalBilling(@McpArg(name = "customerId", description = "l'identifiant du client") Long customerId){
        List<Bill> customerBills = billRepository.findAll().stream()
                .filter(bill -> bill.getCustomerId().equals(customerId))
                .collect(Collectors.toList());
        
        return customerBills.stream()
                .mapToDouble(bill -> bill.getProductItems().stream()
                        .mapToDouble(item -> item.getUnitPrice() * item.getQuantity())
                        .sum())
                .sum();
    }

    @McpTool(name = "getBillsWithTotalAbove", description = "Retourner les factures dont le montant total dépasse un certain seuil")
    public List<Bill> getBillsWithTotalAbove(@McpArg(name = "threshold", description = "le montant minimum de la facture") Double threshold){
        return billRepository.findAll().stream()
                .filter(bill -> {
                    double total = bill.getProductItems().stream()
                            .mapToDouble(item -> item.getUnitPrice() * item.getQuantity())
                            .sum();
                    return total >= threshold;
                })
                .collect(Collectors.toList());
    }

    @McpTool(name = "getTopCustomersByBilling", description = "Retourner les N clients ayant le plus grand montant facturé")
    public Map<Long, Double> getTopCustomersByBilling(@McpArg(name = "limit", description = "le nombre de clients à retourner") Integer limit){
        Map<Long, Double> customerTotals = new HashMap<>();
        
        billRepository.findAll().forEach(bill -> {
            Long customerId = bill.getCustomerId();
            double billTotal = bill.getProductItems().stream()
                    .mapToDouble(item -> item.getUnitPrice() * item.getQuantity())
                    .sum();
            
            customerTotals.merge(customerId, billTotal, Double::sum);
        });
        
        return customerTotals.entrySet().stream()
                .sorted(Map.Entry.<Long, Double>comparingByValue().reversed())
                .limit(limit != null ? limit : 10)
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (e1, e2) -> e1,
                        LinkedHashMap::new
                ));
    }

    @McpTool(name = "getBillCountByCustomer", description = "Compter le nombre de factures pour un client spécifique")
    public Long getBillCountByCustomer(@McpArg(name = "customerId", description = "l'identifiant du client") Long customerId){
        return billRepository.findAll().stream()
                .filter(bill -> bill.getCustomerId().equals(customerId))
                .count();
    }

    @McpTool(name = "getBillsByDate", description = "Retourner les factures créées à une date spécifique")
    public List<Bill> getBillsByDate(@McpArg(name = "date", description = "la date (format: yyyy-MM-dd)") String dateStr){
        try {
            java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd");
            Date targetDate = sdf.parse(dateStr);
            
            Calendar cal = Calendar.getInstance();
            cal.setTime(targetDate);
            cal.add(Calendar.DAY_OF_MONTH, 1);
            Date nextDay = cal.getTime();
            
            return billRepository.findAll().stream()
                    .filter(bill -> bill.getBillingDate() != null)
                    .filter(bill -> {
                        Date billDate = bill.getBillingDate();
                        return !billDate.before(targetDate) && billDate.before(nextDay);
                    })
                    .collect(Collectors.toList());
        } catch (Exception e) {
            return Collections.emptyList();
        }
    }

}
