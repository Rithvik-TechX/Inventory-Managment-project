package com.inventory.database_system.repository;

import com.inventory.database_system.entity.Supplier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SupplierRepository extends JpaRepository<Supplier, Long> {

    List<Supplier> findByActiveTrue();

    List<Supplier> findByActiveFalse();

    @Query("SELECT s FROM Supplier s WHERE s.name LIKE %:keyword% OR s.contactEmail LIKE %:keyword%")
    List<Supplier> searchSuppliers(@Param("keyword") String keyword);

    @Query("SELECT s FROM Supplier s LEFT JOIN FETCH s.products WHERE s.id = :id")
    java.util.Optional<Supplier> findByIdWithProducts(@Param("id") Long id);

    boolean existsByContactEmail(String email);

    @Query("SELECT s, COUNT(t) FROM Supplier s LEFT JOIN s.transactions t GROUP BY s ORDER BY COUNT(t) DESC")
    List<Object[]> findSuppliersWithTransactionCount();
}
