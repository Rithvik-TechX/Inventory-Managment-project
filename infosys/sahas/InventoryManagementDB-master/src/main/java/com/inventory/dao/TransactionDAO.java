package com.inventory.dao;

import com.inventory.model.Transaction;
import java.util.List;

public interface TransactionDAO {
    boolean addTransaction(Transaction transaction);
    Transaction getTransactionById(int id);
    List<Transaction> getAllTransactions();
    List<Transaction> getTransactionsByUser(int userId);
    List<Transaction> getTransactionsByProduct(int productId);
    List<Transaction> getTransactionsByType(String type);
    boolean updateTransactionStatus(int id, String status);
    boolean deleteTransaction(int id);
}