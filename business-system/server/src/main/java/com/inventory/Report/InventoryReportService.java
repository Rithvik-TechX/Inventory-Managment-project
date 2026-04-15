package com.inventory.Report;
import com.database.DatabaseConnection;
import com.inventory.dao.ProductDAO;
import com.inventory.model.Product;
import org.springframework.stereotype.Service;

import java.sql.*;
import java.util.*;
@Service
public class InventoryReportService {

    private final ProductDAO productDAO;

    public InventoryReportService(ProductDAO productDAO) {
        this.productDAO = productDAO;
    }
    public void checkAndSendLowStockAlert(){
        try {
            List<Product> allProducts = productDAO.getAllProducts();
            List<String> lowStockLines = new ArrayList<>();

            for(Product product : allProducts){
                System.out.println(
                        product.getId() +"|"+
                        product.getName()+"|"+
                        product.getStock()+"|"+
                        product.getPrice()+"|"+
                        product.getMinQuantity()
                );
                if(product.getStock() < product.getMinQuantity()){
                    lowStockLines.add(
                            String.format(" - %s (ID: %d) | Category: %s | Stock: %d | Min: %d | Price: %.2f",
                                    product.getName(),
                                    product.getId(),
                                    product.getCategory(),
                                    product.getStock(),
                                    product.getMinQuantity(),
                                    product.getPrice())
                    );
                }
        }

            // only sends email if there are low stock items
            if (!lowStockLines.isEmpty()) {
                sendLowStockAlert(lowStockLines);
            } else {
                System.out.println("All products are sufficiently stocked.");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void sendLowStockAlert(List<String> lowStockLines) {
        EmailService emailService = new EmailService();

        String subject = "LOW STOCK ALERT — Action Required";

        StringBuilder body = new StringBuilder();
        body.append("Hello,\n\n");
        body.append("The following products have dropped below their minimum quantity threshold:\n\n");
        for (String line : lowStockLines) {
            body.append(line).append("\n");
        }
        body.append("\nPlease restock these items as soon as possible.\n\n");
        body.append("-- Inventory System");

        emailService.sendEmail(
                "",   // ← Change this email to get message to your email
                subject,
                body.toString()
        );

        System.out.println("Low stock alert sent for " + lowStockLines.size() + " product(s).");
    }
}
