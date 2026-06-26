package com.inventory.database_system.dto;

import com.inventory.database_system.entity.Transaction;
import jakarta.validation.constraints.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class TransactionDTO {
    private Long id;
    @Min(1) private int quantity;
    private BigDecimal totalPrice;
    @NotNull private Transaction.TransactionType transactionType;
    private LocalDateTime transactionDate;
    private String referenceId;
    private Transaction.TransactionStatus status;
    private String notes;
    @NotNull private Long productId;
    private String productName;
    private String productSku;
    private Long supplierId;
    private String supplierName;
    @NotNull private Long userId;
    private String userName;
    private LocalDateTime createdAt;
}
