package com.inventory.dto;

import lombok.*;
import java.math.BigDecimal;

public class ReportDTO {

    @Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
    public static class CategoryRevenue {
        private String categoryName;
        private BigDecimal revenue;
    }

    @Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
    public static class MonthlySales {
        private int year;
        private int month;
        private BigDecimal totalRevenue;
        private long transactionCount;
    }

    @Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
    public static class InventoryReport {
        private BigDecimal totalInventoryValue;
        private long totalProducts;
        private long lowStockCount;
    }

    @Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
    public static class ProductStock {
        private String productName;
        private String sku;
        private int currentStock;
        private int reorderLevel;
    }

    @Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
    public static class TopProduct {
        private String productName;
        private BigDecimal totalRevenue;
        private long totalQuantitySold;
    }
}
