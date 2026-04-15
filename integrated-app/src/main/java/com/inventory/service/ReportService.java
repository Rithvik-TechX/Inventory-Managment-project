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

    private final TransactionRepository transactionRepository;
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final SupplierRepository supplierRepository;
    private final UserRepository userRepository;

    public ReportService(TransactionRepository transactionRepository,
                         ProductRepository productRepository,
                         CategoryRepository categoryRepository,
                         SupplierRepository supplierRepository,
                         UserRepository userRepository) {
        this.transactionRepository = transactionRepository;
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.supplierRepository = supplierRepository;
        this.userRepository = userRepository;
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

    // Low stock products
    public List<Product> getLowStockProducts() {
        return productRepository.findAll().stream()
                .filter(Product::isLowStock)
                .collect(Collectors.toList());
    }

    // Low stock alert check (from business-system)
    public void checkAndSendLowStockAlert() {
        List<Product> lowStockProducts = getLowStockProducts();
        if (!lowStockProducts.isEmpty()) {
            System.out.println("⚠ Low stock alert for " + lowStockProducts.size() + " product(s):");
            for (Product p : lowStockProducts) {
                System.out.println("  - " + p.getName() + " (SKU: " + p.getSku()
                        + ") | Stock: " + p.getQuantity() + " | Reorder: " + p.getReorderLevel());
            }
        } else {
            System.out.println("✓ All products are sufficiently stocked.");
        }
    }
}
