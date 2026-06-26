package com.inventory.database_system.mapper;

import com.inventory.database_system.dto.TransactionDTO;
import com.inventory.database_system.entity.Transaction;
import org.springframework.stereotype.Component;

@Component
public class TransactionMapper {

    public TransactionDTO toDTO(Transaction t) {
        if (t == null) return null;
        return TransactionDTO.builder()
                .id(t.getId())
                .quantity(t.getQuantity())
                .totalPrice(t.getTotalPrice())
                .transactionType(t.getTransactionType())
                .transactionDate(t.getTransactionDate())
                .referenceId(t.getReferenceId())
                .status(t.getStatus())
                .notes(t.getNotes())
                .productId(t.getProduct() != null ? t.getProduct().getId() : null)
                .productName(t.getProduct() != null ? t.getProduct().getName() : null)
                .productSku(t.getProduct() != null ? t.getProduct().getSku() : null)
                .supplierId(t.getSupplier() != null ? t.getSupplier().getId() : null)
                .supplierName(t.getSupplier() != null ? t.getSupplier().getName() : null)
                .userId(t.getUser() != null ? t.getUser().getId() : null)
                .userName(t.getUser() != null ? t.getUser().getName() : null)
                .createdAt(t.getCreatedAt())
                .build();
    }

    public Transaction toEntity(TransactionDTO dto) {
        if (dto == null) return null;
        return Transaction.builder()
                .id(dto.getId())
                .quantity(dto.getQuantity())
                .totalPrice(dto.getTotalPrice())
                .transactionType(dto.getTransactionType())
                .transactionDate(dto.getTransactionDate())
                .referenceId(dto.getReferenceId())
                .status(dto.getStatus())
                .notes(dto.getNotes())
                .build();
    }
}
