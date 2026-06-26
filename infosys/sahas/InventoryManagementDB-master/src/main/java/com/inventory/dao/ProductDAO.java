package com.inventory.dao;

import com.inventory.model.Product;
import java.util.List;

public interface ProductDAO {
    boolean addProduct(Product product);
    Product getProductById(int id);
    Product getProductBySku(String sku);
    List<Product> getAllProducts();
    List<Product> getProductsByCategory(int categoryId);
    List<Product> getLowStockProducts(int threshold);
    boolean updateProduct(Product product);
    boolean updateStock(int productId, int newQuantity);
    boolean deleteProduct(int id);
}