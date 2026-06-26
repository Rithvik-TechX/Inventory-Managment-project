package com.inventory.dao.impl;

import com.inventory.config.DBConnection;
import com.inventory.dao.TransactionDAO;
import com.inventory.model.Transaction;
import java.sql.*;
import java.util.*;

public class TransactionDAOImpl implements TransactionDAO {

    @Override
    public boolean addTransaction(Transaction t) {
        String sql = "INSERT INTO transaction (totalProducts,totalPrice,transactionType," +
                "transactionStatus,description,note,product_id,user_id,supplier_id)" +
                " VALUES (?,?,?,?,?,?,?,?,?)";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, t.getTotalProducts());
            ps.setDouble(2, t.getTotalPrice());
            ps.setString(3, t.getTransactionType());
            ps.setString(4, t.getTransactionStatus());
            ps.setString(5, t.getDescription());
            ps.setString(6, t.getNote());
            ps.setInt(7, t.getProductId());
            ps.setInt(8, t.getUserId());
            ps.setInt(9, t.getSupplierId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }

    @Override
    public Transaction getTransactionById(int id) {
        String sql = "SELECT * FROM transaction WHERE id = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return mapRow(rs);
        } catch (SQLException e) { e.printStackTrace(); }
        return null;
    }

    @Override
    public List<Transaction> getAllTransactions() {
        List<Transaction> list = new ArrayList<>();
        String sql = "SELECT * FROM transaction ORDER BY createdAt DESC";
        try (Connection con = DBConnection.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) list.add(mapRow(rs));
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }

    @Override
    public List<Transaction> getTransactionsByUser(int userId) {
        List<Transaction> list = new ArrayList<>();
        String sql = "SELECT * FROM transaction WHERE user_id = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) list.add(mapRow(rs));
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }

    @Override
    public List<Transaction> getTransactionsByProduct(int productId) {
        List<Transaction> list = new ArrayList<>();
        String sql = "SELECT * FROM transaction WHERE product_id = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, productId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) list.add(mapRow(rs));
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }

    @Override
    public List<Transaction> getTransactionsByType(String type) {
        List<Transaction> list = new ArrayList<>();
        String sql = "SELECT * FROM transaction WHERE transactionType = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, type);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) list.add(mapRow(rs));
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }

    @Override
    public boolean updateTransactionStatus(int id, String status) {
        String sql = "UPDATE transaction SET transactionStatus = ? WHERE id = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, status);
            ps.setInt(2, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }

    @Override
    public boolean deleteTransaction(int id) {
        String sql = "DELETE FROM transaction WHERE id = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }

    private Transaction mapRow(ResultSet rs) throws SQLException {
        Transaction t = new Transaction();
        t.setId(rs.getInt("id"));
        t.setTotalProducts(rs.getInt("totalProducts"));
        t.setTotalPrice(rs.getDouble("totalPrice"));
        t.setTransactionType(rs.getString("transactionType"));
        t.setTransactionStatus(rs.getString("transactionStatus"));
        t.setDescription(rs.getString("description"));
        t.setNote(rs.getString("note"));
        t.setUpdatedAt(rs.getTimestamp("updatedAt"));
        t.setCreatedAt(rs.getTimestamp("createdAt"));
        t.setProductId(rs.getInt("product_id"));
        t.setUserId(rs.getInt("user_id"));
        t.setSupplierId(rs.getInt("supplier_id"));
        return t;
    }
}