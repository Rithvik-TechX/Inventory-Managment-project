package com.inventory.controller;

import com.inventory.entity.Transaction;
import com.inventory.dto.TransactionDTO;
import com.inventory.entity.Product;
import com.inventory.entity.Supplier;
import com.inventory.entity.User;
import com.inventory.enums.TransactionStatus;
import com.inventory.enums.TransactionType;
import com.inventory.repository.ProductRepository;
import com.inventory.repository.SupplierRepository;
import com.inventory.repository.UserRepository;
import com.inventory.service.TransactionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

    private final TransactionService transactionService;
    private final ProductRepository productRepository;
    private final SupplierRepository supplierRepository;
    private final UserRepository userRepository;

    public TransactionController(TransactionService transactionService,
                                 ProductRepository productRepository,
                                 SupplierRepository supplierRepository,
                                 UserRepository userRepository) {
        this.transactionService = transactionService;
        this.productRepository = productRepository;
        this.supplierRepository = supplierRepository;
        this.userRepository = userRepository;
    }

    private TransactionDTO toDTO(Transaction t) {
        return TransactionDTO.builder()
                .id(t.getId())
                .quantity(t.getQuantity())
                .totalPrice(t.getTotalPrice())
                .transactionType(t.getTransactionType().name())
                .transactionStatus(t.getTransactionStatus().name())
                .description(t.getDescription())
                .notes(t.getNotes())
                .referenceId(t.getReferenceId())
                .productId(t.getProduct() != null ? t.getProduct().getId() : null)
                .productName(t.getProduct() != null ? t.getProduct().getName() : null)
                .supplierId(t.getSupplier() != null ? t.getSupplier().getId() : null)
                .supplierName(t.getSupplier() != null ? t.getSupplier().getName() : null)
                .userId(t.getUser() != null ? t.getUser().getId() : null)
                .userName(t.getUser() != null ? t.getUser().getName() : null)
                .createdAt(t.getCreatedAt() != null ? t.getCreatedAt().toString() : null)
                .updatedAt(t.getUpdatedAt() != null ? t.getUpdatedAt().toString() : null)
                .build();
    }

    @GetMapping
    public ResponseEntity<List<TransactionDTO>> getAllTransactions() {
        return ResponseEntity.ok(
                transactionService.getAllTransactions().stream()
                        .map(this::toDTO).collect(Collectors.toList()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<TransactionDTO> getTransactionById(@PathVariable Long id) {
        return transactionService.getTransactionById(id)
                .map(t -> ResponseEntity.ok(toDTO(t)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<TransactionDTO> createTransaction(@RequestBody Map<String, Object> body,
                                                            @AuthenticationPrincipal User currentUser) {
        Transaction tx = new Transaction();
        tx.setQuantity(((Number) body.get("quantity")).intValue());
        tx.setTotalPrice(new BigDecimal(String.valueOf(body.get("totalPrice"))));
        tx.setTransactionType(TransactionType.valueOf(((String) body.get("transactionType")).toUpperCase()));
        tx.setTransactionStatus(TransactionStatus.valueOf(
                body.getOrDefault("transactionStatus", "PENDING").toString().toUpperCase()));
        tx.setDescription((String) body.get("description"));
        tx.setNotes((String) body.get("notes"));
        tx.setReferenceId((String) body.get("referenceId"));

        Long productId = Long.valueOf(String.valueOf(body.get("productId")));
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));
        tx.setProduct(product);

        if (body.containsKey("supplierId") && body.get("supplierId") != null) {
            Long supId = Long.valueOf(String.valueOf(body.get("supplierId")));
            supplierRepository.findById(supId).ifPresent(tx::setSupplier);
        }

        tx.setUser(currentUser);
        Transaction saved = transactionService.createTransaction(tx);
        return ResponseEntity.status(HttpStatus.CREATED).body(toDTO(saved));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<TransactionDTO> updateStatus(@PathVariable Long id,
                                                       @RequestBody Map<String, String> body) {
        TransactionStatus status = TransactionStatus.valueOf(body.get("status").toUpperCase());
        return ResponseEntity.ok(toDTO(transactionService.updateStatus(id, status)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deleteTransaction(@PathVariable Long id) {
        transactionService.deleteTransaction(id);
        return ResponseEntity.ok(Map.of("message", "Transaction deleted"));
    }
}
