package com.inventory.database_system.repository;

import com.inventory.database_system.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    Optional<Product> findBySku(String sku);

    boolean existsBySku(String sku);

    List<Product> findByActiveTrue();

    List<Product> findByActiveFalse();

    List<Product> findByCategories_Id(Long categoryId);

    List<Product> findBySupplier_Id(Long supplierId);

    // Low stock products (quantity <= reorderLevel)
    @Query("SELECT p FROM Product p WHERE p.quantity <= p.reorderLevel AND p.active = true")
    List<Product> findLowStockProducts();

    // Out of stock
    @Query("SELECT p FROM Product p WHERE p.quantity = 0 AND p.active = true")
    List<Product> findOutOfStockProducts();

    // Search by name, sku, or description
    @Query("SELECT p FROM Product p WHERE " +
           "(p.name LIKE %:keyword% OR p.sku LIKE %:keyword% OR p.description LIKE %:keyword%) " +
           "AND p.active = true")
    List<Product> searchProducts(@Param("keyword") String keyword);

    // Products in price range
    @Query("SELECT p FROM Product p WHERE p.price BETWEEN :minPrice AND :maxPrice AND p.active = true")
    List<Product> findByPriceRange(@Param("minPrice") BigDecimal minPrice,
                                   @Param("maxPrice") BigDecimal maxPrice);

    // Fetch product with its category and supplier eagerly
    @Query("SELECT p FROM Product p " +
           "LEFT JOIN FETCH p.categories " +
           "LEFT JOIN FETCH p.supplier " +
           "WHERE p.id = :id")
    Optional<Product> findByIdWithDetails(@Param("id") Long id);

    // Update stock quantity
    @Modifying
    @Query("UPDATE Product p SET p.quantity = p.quantity + :delta WHERE p.id = :id")
    int adjustQuantity(@Param("id") Long id, @Param("delta") int delta);

    // Total inventory value
    @Query("SELECT SUM(p.price * p.quantity) FROM Product p WHERE p.active = true")
    BigDecimal calculateTotalInventoryValue();

    // Products with most transactions
    @Query("SELECT p, COUNT(t) as txCount FROM Product p " +
           "LEFT JOIN p.transactions t GROUP BY p ORDER BY txCount DESC")
    List<Object[]> findProductsByTransactionCount();

    // Count by category
    @Query("SELECT p.categories.name, COUNT(p) FROM Product p WHERE p.categories IS NOT NULL GROUP BY p.categories.name")
    List<Object[]> countProductsByCategory();
}
