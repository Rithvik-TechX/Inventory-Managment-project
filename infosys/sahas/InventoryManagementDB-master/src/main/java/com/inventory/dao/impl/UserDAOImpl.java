package com.inventory.dao.impl;

import com.inventory.config.DBConnection;
import com.inventory.dao.UserDAO;
import com.inventory.model.User;
import java.sql.*;
import java.util.*;

public class UserDAOImpl implements UserDAO {

    @Override
    public boolean addUser(User user) {
        String sql = "INSERT INTO user (name,email,password,phoneNumber,role) VALUES (?,?,?,?,?)";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, user.getName());
            ps.setString(2, user.getEmail());
            ps.setString(3, user.getPassword());
            ps.setString(4, user.getPhoneNumber());
            ps.setString(5, user.getRole());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }

    @Override
    public User getUserById(int id) {
        String sql = "SELECT * FROM user WHERE id = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return mapRow(rs);
        } catch (SQLException e) { e.printStackTrace(); }
        return null;
    }

    @Override
    public User getUserByEmail(String email) {
        String sql = "SELECT * FROM user WHERE email = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return mapRow(rs);
        } catch (SQLException e) { e.printStackTrace(); }
        return null;
    }

    @Override
    public List<User> getAllUsers() {
        List<User> list = new ArrayList<>();
        String sql = "SELECT * FROM user";
        try (Connection con = DBConnection.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) list.add(mapRow(rs));
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }

    @Override
    public boolean updateUser(User user) {
        String sql = "UPDATE user SET name=?,email=?,phoneNumber=?,role=? WHERE id=?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, user.getName());
            ps.setString(2, user.getEmail());
            ps.setString(3, user.getPhoneNumber());
            ps.setString(4, user.getRole());
            ps.setInt(5, user.getId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }

    @Override
    public boolean deleteUser(int id) {
        String checkSQL  = "SELECT COUNT(*) FROM transaction WHERE user_id = ?";
        String deleteSQL = "DELETE FROM user WHERE id = ?";

        try (Connection con = DBConnection.getConnection()) {

            // Step 1 — Check if transactions exist for this user
            PreparedStatement ps1 = con.prepareStatement(checkSQL);
            ps1.setInt(1, id);
            ResultSet rs = ps1.executeQuery();
            rs.next();
            int count = rs.getInt(1);

            // Step 2 — Block delete if transactions exist
            if (count > 0) {
                System.out.println("⚠️ Cannot delete! " + count +
                        " transaction(s) linked to this user.");
                return false;
            }

            // Step 3 — Safe to delete
            PreparedStatement ps2 = con.prepareStatement(deleteSQL);
            ps2.setInt(1, id);
            return ps2.executeUpdate() > 0;

        } catch (SQLException e) { e.printStackTrace(); return false; }
    }

    private User mapRow(ResultSet rs) throws SQLException {
        User u = new User();
        u.setId(rs.getInt("id"));
        u.setName(rs.getString("name"));
        u.setEmail(rs.getString("email"));
        u.setPassword(rs.getString("password"));
        u.setPhoneNumber(rs.getString("phoneNumber"));
        u.setRole(rs.getString("role"));
        u.setCreatedAt(rs.getTimestamp("createdAt"));
        return u;
    }
}