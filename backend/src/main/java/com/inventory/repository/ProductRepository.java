package com.inventory.repository;

import com.inventory.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    Optional<Product> findBySku(String sku);

    boolean existsBySku(String sku);

    List<Product> findByActiveTrue();

    List<Product> findByCategoryId(Long categoryId);
    long countByCategoryId(Long categoryId);

    List<Product> findBySupplierId(Long supplierId);

    List<Product> findByNameContainingIgnoreCase(String name);

    List<Product> findByQuantityLessThanEqual(Integer reorderLevel);
}
