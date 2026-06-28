package com.inventory.dto;

import lombok.*;
import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TransactionDTO {

    private Long id;
    private Integer quantity;
    private BigDecimal totalPrice;
    private String transactionType;
    private String transactionStatus;
    private String description;
    private String notes;
    private String referenceId;
    private Long productId;
    private String productName;
    private Long supplierId;
    private String supplierName;
    private Long userId;
    private String userName;

    private String createdAt;
    private String updatedAt;
    private String expectedDelivery;
}
