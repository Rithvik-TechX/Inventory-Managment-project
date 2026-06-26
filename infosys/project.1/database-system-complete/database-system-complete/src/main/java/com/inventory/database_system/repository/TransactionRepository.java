package com.inventory.database_system.repository;

import com.inventory.database_system.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    List<Transaction> findByProduct_Id(Long productId);

    List<Transaction> findBySupplier_Id(Long supplierId);

    List<Transaction> findByUser_Id(Long userId);

    List<Transaction> findByTransactionType(Transaction.TransactionType type);

    List<Transaction> findByStatus(Transaction.TransactionStatus status);

    Optional<Transaction> findByReferenceId(String referenceId);

    // Date range queries
    @Query("SELECT t FROM Transaction t WHERE t.transactionDate BETWEEN :from AND :to ORDER BY t.transactionDate DESC")
    List<Transaction> findByDateRange(@Param("from") LocalDateTime from, @Param("to") LocalDateTime to);

    // Fetch all related entities (avoids N+1)
    @Query("SELECT t FROM Transaction t " +
           "LEFT JOIN FETCH t.product " +
           "LEFT JOIN FETCH t.supplier " +
           "LEFT JOIN FETCH t.user " +
           "WHERE t.id = :id")
    Optional<Transaction> findByIdWithDetails(@Param("id") Long id);

    // Revenue: sum of SALE transactions in date range
    @Query("SELECT COALESCE(SUM(t.totalPrice), 0) FROM Transaction t " +
           "WHERE t.transactionType = 'SALE' " +
           "AND t.status = 'COMPLETED' " +
           "AND t.transactionDate BETWEEN :from AND :to")
    BigDecimal calculateRevenueBetween(@Param("from") LocalDateTime from, @Param("to") LocalDateTime to);

    // Cost: sum of PURCHASE transactions in date range
    @Query("SELECT COALESCE(SUM(t.totalPrice), 0) FROM Transaction t " +
           "WHERE t.transactionType = 'PURCHASE' " +
           "AND t.status = 'COMPLETED' " +
           "AND t.transactionDate BETWEEN :from AND :to")
    BigDecimal calculatePurchaseCostBetween(@Param("from") LocalDateTime from, @Param("to") LocalDateTime to);

    // Daily sales report: total per day
    @Query("SELECT DATE(t.transactionDate), SUM(t.totalPrice), COUNT(t) " +
           "FROM Transaction t " +
           "WHERE t.transactionType = 'SALE' AND t.status = 'COMPLETED' " +
           "AND t.transactionDate BETWEEN :from AND :to " +
           "GROUP BY DATE(t.transactionDate) ORDER BY DATE(t.transactionDate)")
    List<Object[]> getDailySalesReport(@Param("from") LocalDateTime from, @Param("to") LocalDateTime to);

    // Top selling products by quantity
    @Query("SELECT t.product.id, t.product.name, SUM(t.quantity) as totalQty " +
           "FROM Transaction t " +
           "WHERE t.transactionType = 'SALE' AND t.status = 'COMPLETED' " +
           "GROUP BY t.product.id, t.product.name ORDER BY totalQty DESC")
    List<Object[]> findTopSellingProducts();

    // Transaction count per user
    @Query("SELECT t.user.id, t.user.name, COUNT(t) FROM Transaction t GROUP BY t.user.id, t.user.name")
    List<Object[]> countTransactionsByUser();

    // Monthly summary
    @Query(value = "SELECT YEAR(transaction_date) as yr, MONTH(transaction_date) as mo, " +
                   "transaction_type, SUM(total_price), COUNT(*) " +
                   "FROM transactions WHERE status = 'COMPLETED' " +
                   "GROUP BY yr, mo, transaction_type ORDER BY yr, mo",
           nativeQuery = true)
    List<Object[]> getMonthlySummary();

    // Recent transactions (latest N)
    @Query("SELECT t FROM Transaction t ORDER BY t.transactionDate DESC")
    List<Transaction> findRecentTransactions(org.springframework.data.domain.Pageable pageable);
}
