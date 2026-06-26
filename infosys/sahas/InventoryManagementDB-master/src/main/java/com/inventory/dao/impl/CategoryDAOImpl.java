package com.inventory.dao.impl;

import com.inventory.config.DBConnection;
import com.inventory.dao.CategoryDAO;
import com.inventory.model.Category;
import java.sql.*;
import java.util.*;

public class CategoryDAOImpl implements CategoryDAO {

    @Override
    public boolean addCategory(Category category) {
        String sql = "INSERT INTO category (name) VALUES (?)";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, category.getName());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }

    @Override
    public Category getCategoryById(int id) {
        String sql = "SELECT * FROM category WHERE id = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return mapRow(rs);
        } catch (SQLException e) { e.printStackTrace(); }
        return null;
    }

    @Override
    public List<Category> getAllCategories() {
        List<Category> list = new ArrayList<>();
        String sql = "SELECT * FROM category";
        try (Connection con = DBConnection.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) list.add(mapRow(rs));
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }

    @Override
    public boolean updateCategory(Category category) {
        String sql = "UPDATE category SET name = ? WHERE id = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, category.getName());
            ps.setInt(2, category.getId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }

    @Override
    public boolean deleteCategory(int id) {
        String checkSQL  = "SELECT COUNT(*) FROM product WHERE category_id = ?";
        String deleteSQL = "DELETE FROM category WHERE id = ?";

        try (Connection con = DBConnection.getConnection()) {
            PreparedStatement ps1 = con.prepareStatement(checkSQL);
            ps1.setInt(1, id);
            ResultSet rs = ps1.executeQuery();
            rs.next();
            int count = rs.getInt(1);

            if (count > 0) {
                System.out.println("⚠️ Cannot delete! " + count +
                        " product(s) linked to this category.");
                return false;
            }

            PreparedStatement ps2 = con.prepareStatement(deleteSQL);
            ps2.setInt(1, id);
            return ps2.executeUpdate() > 0;

        } catch (SQLException e) { e.printStackTrace(); return false; }
    }

    private Category mapRow(ResultSet rs) throws SQLException {
        Category c = new Category();
        c.setId(rs.getInt("id"));
        c.setName(rs.getString("name"));
        return c;
    }
}