package com.inventory.service;

import com.inventory.entity.Product;
import com.inventory.entity.Transaction;
import com.inventory.enums.TransactionStatus;
import com.inventory.enums.TransactionType;
import com.inventory.repository.ProductRepository;
import com.inventory.repository.TransactionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final ProductRepository productRepository;
    private final ProductService productService;

    public TransactionService(TransactionRepository transactionRepository,
                              ProductRepository productRepository,
                              ProductService productService) {
        this.transactionRepository = transactionRepository;
        this.productRepository = productRepository;
        this.productService = productService;
    }

    // Create transaction and auto-update product stock
    public Transaction createTransaction(Transaction transaction) {
        Product product = transaction.getProduct();
        if (product == null || product.getId() == null) {
            throw new RuntimeException("Transaction must have a valid product");
        }

        Long productId = product.getId();
        product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + productId));

        // Update stock based on transaction type
        if (transaction.getTransactionType() == TransactionType.SALE) {
            if (product.getQuantity() < transaction.getQuantity()) {
                throw new RuntimeException("Insufficient stock. Available: " + product.getQuantity()
                        + ", Requested: " + transaction.getQuantity());
            }
            product.setQuantity(product.getQuantity() - transaction.getQuantity());
        } else if (transaction.getTransactionType() == TransactionType.PURCHASE
                && transaction.getTransactionStatus() == TransactionStatus.COMPLETED) {
            product.setQuantity(product.getQuantity() + transaction.getQuantity());
        } else if (transaction.getTransactionType() == TransactionType.RETURN) {
            product.setQuantity(product.getQuantity() + transaction.getQuantity());
        }
        // ADJUSTMENT — stock is managed manually, no auto change

        productRepository.save(product);
        Transaction saved = transactionRepository.save(transaction);

        productService.checkAndSendLowStockAlert(product);

        return saved;
    }

    @Transactional(readOnly = true)
    public List<Transaction> getAllTransactions() { return transactionRepository.findAll(); }

    @Transactional(readOnly = true)
    public Optional<Transaction> getTransactionById(Long id) { return transactionRepository.findById(id); }

    @Transactional(readOnly = true)
    public Optional<Transaction> getTransactionByReferenceId(String referenceId) {
        return transactionRepository.findByReferenceId(referenceId);
    }

    @Transactional(readOnly = true)
    public List<Transaction> getTransactionsByType(TransactionType type) {
        return transactionRepository.findByTransactionType(type);
    }

    @Transactional(readOnly = true)
    public List<Transaction> getTransactionsByStatus(TransactionStatus status) {
        return transactionRepository.findByTransactionStatus(status);
    }

    @Transactional(readOnly = true)
    public List<Transaction> getTransactionsByProduct(Long productId) {
        return transactionRepository.findByProductId(productId);
    }

    @Transactional(readOnly = true)
    public List<Transaction> getTransactionsByUser(Long userId) {
        return transactionRepository.findByUserId(userId);
    }

    @Transactional(readOnly = true)
    public List<Transaction> getTransactionsBySupplier(Long supplierId) {
        return transactionRepository.findBySupplierId(supplierId);
    }

    @Transactional(readOnly = true)
    public List<Transaction> getTransactionsByDateRange(LocalDateTime start, LocalDateTime end) {
        return transactionRepository.findByCreatedAtBetween(start, end);
    }

    public Transaction updateStatus(Long id, TransactionStatus status) {
        Transaction transaction = transactionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Transaction not found with id: " + id));
        if (transaction.getTransactionType() == TransactionType.PURCHASE
                && transaction.getTransactionStatus() != TransactionStatus.COMPLETED
                && status == TransactionStatus.COMPLETED) {
            Product product = transaction.getProduct();
            product.setQuantity(product.getQuantity() + transaction.getQuantity());
            productRepository.save(product);
            productService.checkAndSendLowStockAlert(product);
        }
        transaction.setTransactionStatus(status);
        return transactionRepository.save(transaction);
    }

    public void deleteTransaction(Long id) {
        if (!transactionRepository.existsById(id)) {
            throw new RuntimeException("Transaction not found with id: " + id);
        }
        transactionRepository.deleteById(id);
    }
}
