package com.inventory.database_system.service;

import com.inventory.database_system.dto.ReportDTO;
import com.inventory.database_system.entity.Transaction;
import com.inventory.database_system.repository.ProductRepository;
import com.inventory.database_system.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReportService {

    private final ProductRepository productRepository;
    private final TransactionRepository transactionRepository;

    public ReportDTO generateFullReport(LocalDateTime from, LocalDateTime to) {

        // Inventory stats
        long totalProducts = productRepository.count();
        long activeProducts = productRepository.findByActiveTrue().size();
        long lowStock = productRepository.findLowStockProducts().size();
        long outOfStock = productRepository.findOutOfStockProducts().size();
        BigDecimal inventoryValue = productRepository.calculateTotalInventoryValue();

        // Transaction stats
        long totalTx = transactionRepository.count();
        long completed = transactionRepository.findByStatus(Transaction.TransactionStatus.COMPLETED).size();
        long pending = transactionRepository.findByStatus(Transaction.TransactionStatus.PENDING).size();
        BigDecimal revenue = transactionRepository.calculateRevenueBetween(from, to);
        BigDecimal purchaseCost = transactionRepository.calculatePurchaseCostBetween(from, to);

        // Category breakdown
        List<Object[]> categoryData = productRepository.countProductsByCategory();
        Map<String, Long> byCategory = new HashMap<>();
        for (Object[] row : categoryData) {
            byCategory.put((String) row[0], (Long) row[1]);
        }

        // Top selling products
        List<Object[]> topSelling = transactionRepository.findTopSellingProducts();
        List<ReportDTO.TopProductDTO> topProducts = topSelling.stream()
                .limit(10)
                .map(row -> ReportDTO.TopProductDTO.builder()
                        .productId((Long) row[0])
                        .productName((String) row[1])
                        .totalQuantitySold(((Number) row[2]).longValue())
                        .build())
                .collect(Collectors.toList());

        // Daily sales
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        List<Object[]> dailyData = transactionRepository.getDailySalesReport(from, to);
        List<ReportDTO.DailySalesDTO> dailySales = dailyData.stream()
                .map(row -> ReportDTO.DailySalesDTO.builder()
                        .date(row[0].toString())
                        .totalAmount((BigDecimal) row[1])
                        .transactionCount(((Number) row[2]).longValue())
                        .build())
                .collect(Collectors.toList());

        BigDecimal netProfit = (revenue != null ? revenue : BigDecimal.ZERO)
                .subtract(purchaseCost != null ? purchaseCost : BigDecimal.ZERO);

        return ReportDTO.builder()
                .totalProducts(totalProducts)
                .activeProducts(activeProducts)
                .lowStockProducts(lowStock)
                .outOfStockProducts(outOfStock)
                .totalInventoryValue(inventoryValue != null ? inventoryValue : BigDecimal.ZERO)
                .totalRevenue(revenue != null ? revenue : BigDecimal.ZERO)
                .totalPurchaseCost(purchaseCost != null ? purchaseCost : BigDecimal.ZERO)
                .netProfit(netProfit)
                .totalTransactions(totalTx)
                .completedTransactions(completed)
                .pendingTransactions(pending)
                .productsByCategory(byCategory)
                .topSellingProducts(topProducts)
                .dailySales(dailySales)
                .build();
    }
}
