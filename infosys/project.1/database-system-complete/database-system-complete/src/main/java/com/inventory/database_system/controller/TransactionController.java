package com.inventory.database_system.controller;

import com.inventory.database_system.dto.ApiResponse;
import com.inventory.database_system.dto.TransactionDTO;
import com.inventory.database_system.entity.Transaction;
import com.inventory.database_system.service.TransactionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/transactions")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<TransactionDTO>>> getAll() {
        return ResponseEntity.ok(ApiResponse.success(transactionService.getAllTransactions(), "Transactions fetched"));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<TransactionDTO>> getById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(transactionService.getTransactionById(id), "Transaction found"));
    }

    @GetMapping("/product/{productId}")
    public ResponseEntity<ApiResponse<List<TransactionDTO>>> getByProduct(@PathVariable Long productId) {
        return ResponseEntity.ok(ApiResponse.success(transactionService.getByProduct(productId), "Transactions by product"));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<ApiResponse<List<TransactionDTO>>> getByUser(@PathVariable Long userId) {
        return ResponseEntity.ok(ApiResponse.success(transactionService.getByUser(userId), "Transactions by user"));
    }

    @GetMapping("/date-range")
    public ResponseEntity<ApiResponse<List<TransactionDTO>>> getByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime from,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime to) {
        return ResponseEntity.ok(ApiResponse.success(transactionService.getByDateRange(from, to), "Transactions in range"));
    }

    @GetMapping("/recent")
    public ResponseEntity<ApiResponse<List<TransactionDTO>>> getRecent(
            @RequestParam(defaultValue = "10") int limit) {
        return ResponseEntity.ok(ApiResponse.success(transactionService.getRecentTransactions(limit), "Recent transactions"));
    }

    @GetMapping("/revenue")
    public ResponseEntity<ApiResponse<BigDecimal>> getRevenue(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime from,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime to) {
        return ResponseEntity.ok(ApiResponse.success(transactionService.getRevenueBetween(from, to), "Revenue"));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<TransactionDTO>> create(@Valid @RequestBody TransactionDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(transactionService.createTransaction(dto), "Transaction created"));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<ApiResponse<TransactionDTO>> updateStatus(
            @PathVariable Long id, @RequestParam Transaction.TransactionStatus status) {
        return ResponseEntity.ok(ApiResponse.success(transactionService.updateStatus(id, status), "Status updated"));
    }
}
