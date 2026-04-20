package com.inventory.service;

import com.inventory.dto.DashboardDTO;
import com.inventory.entity.Product;
import com.inventory.enums.TransactionStatus;
import com.inventory.enums.TransactionType;
import com.inventory.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class ReportService {

    private static final String ADMIN_EMAIL = "rithvikgandhamalla14@gmail.com";

    private final TransactionRepository transactionRepository;
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final SupplierRepository supplierRepository;
    private final UserRepository userRepository;
    private final EmailService emailService;

    public ReportService(TransactionRepository transactionRepository,
                         ProductRepository productRepository,
                         CategoryRepository categoryRepository,
                         SupplierRepository supplierRepository,
                         UserRepository userRepository,
                         EmailService emailService) {
        this.transactionRepository = transactionRepository;
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.supplierRepository = supplierRepository;
        this.userRepository = userRepository;
        this.emailService = emailService;
    }

    // Dashboard summary
    public DashboardDTO getDashboardSummary() {
        return DashboardDTO.builder()
                .totalProducts(productRepository.count())
                .totalCategories(categoryRepository.count())
                .totalSuppliers(supplierRepository.count())
                .totalUsers(userRepository.count())
                .totalTransactions(transactionRepository.count())
                .salesTransactions(transactionRepository.findByTransactionType(TransactionType.SALE).size())
                .purchaseTransactions(transactionRepository.findByTransactionType(TransactionType.PURCHASE).size())
                .pendingTransactions(transactionRepository.findByTransactionStatus(TransactionStatus.PENDING).size())
                .build();
    }

    // Low stock products (quantity < 50% of maxStock)
    public List<Product> getLowStockProducts() {
        return productRepository.findAll().stream()
                .filter(Product::isLowStock)
                .collect(Collectors.toList());
    }

    // Low stock alert check — sends email to admin if any products are low
    @Transactional
    public void checkAndSendLowStockAlert() {
        List<Product> lowStockProducts = getLowStockProducts();
        if (!lowStockProducts.isEmpty()) {
            System.out.println("⚠ Low stock alert for " + lowStockProducts.size() + " product(s):");

            StringBuilder emailBody = new StringBuilder();
            emailBody.append("LOW STOCK ALERT — Inventory Management System\n");
            emailBody.append("=================================================\n\n");
            emailBody.append("The following ").append(lowStockProducts.size())
                     .append(" product(s) have dropped below 50% of their maximum stock level:\n\n");

            for (Product p : lowStockProducts) {
                int threshold = (int) (p.getMaxStock() * 0.5);
                System.out.println("  - " + p.getName() + " (SKU: " + p.getSku()
                        + ") | Stock: " + p.getQuantity() + " / " + p.getMaxStock()
                        + " | Threshold (50%): " + threshold);

                emailBody.append("Product: ").append(p.getName()).append("\n")
                         .append("   SKU: ").append(p.getSku()).append("\n")
                         .append("   Current Stock: ").append(p.getQuantity()).append("\n")
                         .append("   Max Stock: ").append(p.getMaxStock()).append("\n")
                         .append("   Low Stock Threshold (50%): ").append(threshold).append("\n")
                         .append("   Category: ").append(p.getCategory() != null ? p.getCategory().getName() : "N/A").append("\n")
                         .append("   Supplier: ").append(p.getSupplier() != null ? p.getSupplier().getName() : "N/A").append("\n\n");
            }

            emailBody.append("=================================================\n");
            emailBody.append("Please restock these items at the earliest.\n");
            emailBody.append("— Inventory Management System (Automated Alert)");

            // Send email to admin
            try {
                emailService.sendEmail(
                    ADMIN_EMAIL,
                    "Low Stock Alert — " + lowStockProducts.size() + " Product(s) Need Restocking",
                    emailBody.toString()
                );
                System.out.println("Email: Low stock alert sent to " + ADMIN_EMAIL);
            } catch (Exception e) {
                System.err.println("Failed to send low stock email: " + e.getMessage());
            }
        } else {
            System.out.println("All products are sufficiently stocked.");
        }
    }
}
