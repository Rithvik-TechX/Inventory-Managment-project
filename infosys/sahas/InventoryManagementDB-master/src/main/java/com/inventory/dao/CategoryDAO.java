package com.inventory.dao;

import com.inventory.model.Category;
import java.util.List;

public interface CategoryDAO {
    boolean addCategory(Category category);
    Category getCategoryById(int id);
    List<Category> getAllCategories();
    boolean updateCategory(Category category);
    boolean deleteCategory(int id);
}