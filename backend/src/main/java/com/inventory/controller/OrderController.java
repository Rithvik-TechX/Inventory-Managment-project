package com.inventory.controller;

import com.inventory.dto.TransactionDTO;
import com.inventory.entity.*;
import com.inventory.enums.TransactionStatus;
import com.inventory.enums.TransactionType;
import com.inventory.repository.ProductRepository;
import com.inventory.repository.SupplierRepository;
import com.inventory.service.TransactionService;
import org.springframework.http.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/orders")
public class OrderController {
    private final TransactionService transactionService;
    private final ProductRepository productRepository;
    private final SupplierRepository supplierRepository;

    public OrderController(TransactionService transactionService, ProductRepository productRepository,
                           SupplierRepository supplierRepository) {
        this.transactionService = transactionService; this.productRepository = productRepository;
        this.supplierRepository = supplierRepository;
    }

    @GetMapping
    public List<TransactionDTO> all() {
        return transactionService.getTransactionsByType(TransactionType.PURCHASE).stream().map(this::dto).collect(Collectors.toList());
    }

    @PostMapping
    public ResponseEntity<TransactionDTO> create(@RequestBody Map<String,Object> body,
                                                  @AuthenticationPrincipal User user) {
        Product product = productRepository.findById(asLong(body.get("productId"))).orElseThrow(() -> new RuntimeException("Product not found"));
        Supplier supplier = supplierRepository.findById(asLong(body.get("supplierId"))).orElseThrow(() -> new RuntimeException("Supplier not found"));
        int quantity = ((Number) body.get("quantity")).intValue();
        if (quantity < 1) throw new IllegalArgumentException("Quantity must be at least 1");
        Transaction order = new Transaction();
        order.setProduct(product); order.setSupplier(supplier); order.setUser(user); order.setQuantity(quantity);
        order.setTotalPrice(product.getPrice().multiply(BigDecimal.valueOf(quantity)));
        order.setTransactionType(TransactionType.PURCHASE); order.setTransactionStatus(TransactionStatus.PENDING);
        order.setNotes((String) body.get("notes")); order.setDescription("Purchase order");
        order.setReferenceId("ORD-" + UUID.randomUUID().toString().substring(0,8).toUpperCase());
        if (body.get("expectedDelivery") != null && !body.get("expectedDelivery").toString().isBlank())
            order.setExpectedDelivery(LocalDate.parse(body.get("expectedDelivery").toString()));
        return ResponseEntity.status(HttpStatus.CREATED).body(dto(transactionService.createTransaction(order)));
    }

    @PatchMapping("/{id}/status")
    public TransactionDTO status(@PathVariable Long id, @RequestBody Map<String,String> body) {
        String raw = body.get("status").toUpperCase();
        TransactionStatus status = raw.equals("RECEIVED") ? TransactionStatus.COMPLETED : TransactionStatus.valueOf(raw);
        return dto(transactionService.updateStatus(id,status));
    }

    private Long asLong(Object value) { return Long.valueOf(String.valueOf(value)); }
    private TransactionDTO dto(Transaction t) {
        return TransactionDTO.builder().id(t.getId()).quantity(t.getQuantity()).totalPrice(t.getTotalPrice())
                .transactionType(t.getTransactionType().name()).transactionStatus(t.getTransactionStatus().name())
                .description(t.getDescription()).notes(t.getNotes()).referenceId(t.getReferenceId())
                .productId(t.getProduct().getId()).productName(t.getProduct().getName())
                .supplierId(t.getSupplier().getId()).supplierName(t.getSupplier().getName())
                .userId(t.getUser()==null?null:t.getUser().getId()).userName(t.getUser()==null?null:t.getUser().getName())
                .createdAt(t.getCreatedAt()==null?null:t.getCreatedAt().toString())
                .updatedAt(t.getUpdatedAt()==null?null:t.getUpdatedAt().toString())
                .expectedDelivery(t.getExpectedDelivery()==null?null:t.getExpectedDelivery().toString()).build();
    }
}
