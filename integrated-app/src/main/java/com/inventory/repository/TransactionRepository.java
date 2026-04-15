package com.inventory.repository;

import com.inventory.entity.Transaction;
import com.inventory.enums.TransactionStatus;
import com.inventory.enums.TransactionType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    List<Transaction> findByTransactionType(TransactionType type);

    List<Transaction> findByTransactionStatus(TransactionStatus status);

    List<Transaction> findByProductId(Long productId);

    List<Transaction> findByUserId(Long userId);

    List<Transaction> findBySupplierId(Long supplierId);

    List<Transaction> findByCreatedAtBetween(LocalDateTime start, LocalDateTime end);

    List<Transaction> findByTransactionTypeAndCreatedAtBetween(
            TransactionType type, LocalDateTime start, LocalDateTime end);

    Optional<Transaction> findByReferenceId(String referenceId);
}
