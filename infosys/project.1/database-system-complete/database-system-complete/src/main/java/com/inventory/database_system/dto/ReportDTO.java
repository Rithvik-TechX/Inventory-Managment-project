package com.inventory.database_system.dto;

import lombok.*;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class ReportDTO {
    private long totalProducts;
    private long activeProducts;
    private long lowStockProducts;
    private long outOfStockProducts;
    private BigDecimal totalInventoryValue;
    private BigDecimal totalRevenue;
    private BigDecimal totalPurchaseCost;
    private BigDecimal netProfit;
    private long totalTransactions;
    private long completedTransactions;
    private long pendingTransactions;
    private Map<String, Long> productsByCategory;
    private List<TopProductDTO> topSellingProducts;
    private List<DailySalesDTO> dailySales;

    @Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
    public static class TopProductDTO {
        private Long productId;
        private String productName;
        private long totalQuantitySold;
    }

    @Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
    public static class DailySalesDTO {
        private String date;
        private BigDecimal totalAmount;
        private long transactionCount;
    }
}
