package com.inventory.database_system.service;

import com.inventory.database_system.dto.TransactionDTO;
import com.inventory.database_system.entity.Product;
import com.inventory.database_system.entity.Supplier;
import com.inventory.database_system.entity.Transaction;
import com.inventory.database_system.entity.User;
import com.inventory.database_system.exception.InsufficientStockException;
import com.inventory.database_system.exception.ResourceNotFoundException;
import com.inventory.database_system.mapper.TransactionMapper;
import com.inventory.database_system.repository.ProductRepository;
import com.inventory.database_system.repository.SupplierRepository;
import com.inventory.database_system.repository.TransactionRepository;
import com.inventory.database_system.repository.UserRepository;
import com.inventory.database_system.util.ReferenceIdGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final ProductRepository productRepository;
    private final SupplierRepository supplierRepository;
    private final UserRepository userRepository;
    private final TransactionMapper transactionMapper;
    private final ReferenceIdGenerator referenceIdGenerator;

    public List<TransactionDTO> getAllTransactions() {
        return transactionRepository.findAll().stream()
                .map(transactionMapper::toDTO).collect(Collectors.toList());
    }

    public TransactionDTO getTransactionById(Long id) {
        Transaction t = transactionRepository.findByIdWithDetails(id)
                .orElseThrow(() -> new ResourceNotFoundException("Transaction", "id", id));
        return transactionMapper.toDTO(t);
    }

    public List<TransactionDTO> getByProduct(Long productId) {
        return transactionRepository.findByProduct_Id(productId).stream()
                .map(transactionMapper::toDTO).collect(Collectors.toList());
    }

    public List<TransactionDTO> getByUser(Long userId) {
        return transactionRepository.findByUser_Id(userId).stream()
                .map(transactionMapper::toDTO).collect(Collectors.toList());
    }

    public List<TransactionDTO> getByDateRange(LocalDateTime from, LocalDateTime to) {
        return transactionRepository.findByDateRange(from, to).stream()
                .map(transactionMapper::toDTO).collect(Collectors.toList());
    }

    public List<TransactionDTO> getRecentTransactions(int limit) {
        return transactionRepository.findRecentTransactions(PageRequest.of(0, limit)).stream()
                .map(transactionMapper::toDTO).collect(Collectors.toList());
    }

    public BigDecimal getRevenueBetween(LocalDateTime from, LocalDateTime to) {
        return transactionRepository.calculateRevenueBetween(from, to);
    }

    public BigDecimal getPurchaseCostBetween(LocalDateTime from, LocalDateTime to) {
        return transactionRepository.calculatePurchaseCostBetween(from, to);
    }

    @Transactional
    public TransactionDTO createTransaction(TransactionDTO dto) {
        Product product = productRepository.findById(dto.getProductId())
                .orElseThrow(() -> new ResourceNotFoundException("Product", "id", dto.getProductId()));

        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", dto.getUserId()));

        // Stock management
        if (dto.getTransactionType() == Transaction.TransactionType.SALE) {
            if (product.getQuantity() < dto.getQuantity()) {
                throw new InsufficientStockException(product.getName(), dto.getQuantity(), product.getQuantity());
            }
            productRepository.adjustQuantity(product.getId(), -dto.getQuantity());
        } else if (dto.getTransactionType() == Transaction.TransactionType.PURCHASE) {
            productRepository.adjustQuantity(product.getId(), dto.getQuantity());
        }

        Transaction transaction = transactionMapper.toEntity(dto);
        transaction.setProduct(product);
        transaction.setUser(user);

        if (dto.getSupplierId() != null) {
            Supplier supplier = supplierRepository.findById(dto.getSupplierId())
                    .orElseThrow(() -> new ResourceNotFoundException("Supplier", "id", dto.getSupplierId()));
            transaction.setSupplier(supplier);
        }

        // Auto-calculate total price if not set
        if (transaction.getTotalPrice() == null || transaction.getTotalPrice().compareTo(BigDecimal.ZERO) == 0) {
            transaction.setTotalPrice(product.getPrice().multiply(BigDecimal.valueOf(dto.getQuantity())));
        }

        // Auto generate reference ID
        if (transaction.getReferenceId() == null || transaction.getReferenceId().isBlank()) {
            transaction.setReferenceId(referenceIdGenerator.generateTransactionRef());
        }

        if (transaction.getStatus() == null) {
            transaction.setStatus(Transaction.TransactionStatus.COMPLETED);
        }

        return transactionMapper.toDTO(transactionRepository.save(transaction));
    }

    @Transactional
    public TransactionDTO updateStatus(Long id, Transaction.TransactionStatus status) {
        Transaction t = transactionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Transaction", "id", id));
        t.setStatus(status);
        return transactionMapper.toDTO(transactionRepository.save(t));
    }
}
