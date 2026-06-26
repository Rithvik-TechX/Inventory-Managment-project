package com.inventory.database_system.repository;

import com.inventory.database_system.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    Optional<Category> findByName(String name);

    boolean existsByName(String name);

    List<Category> findByActiveTrue();

    @Query("SELECT c FROM Category c WHERE c.name LIKE %:keyword%")
    List<Category> searchByName(@Param("keyword") String keyword);

    @Query("SELECT c FROM Category c LEFT JOIN FETCH c.products WHERE c.id = :id")
    Optional<Category> findByIdWithProducts(@Param("id") Long id);

    @Query("SELECT c, COUNT(p) FROM Category c LEFT JOIN c.products p GROUP BY c")
    List<Object[]> findCategoriesWithProductCount();

    @Query("SELECT c FROM Category c WHERE SIZE(c.products) > 0")
    List<Category> findCategoriesWithProducts();
}
