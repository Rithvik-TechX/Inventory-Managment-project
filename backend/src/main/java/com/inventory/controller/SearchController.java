package com.inventory.controller;

import com.inventory.repository.CategoryRepository;
import com.inventory.repository.ProductRepository;
import com.inventory.repository.SupplierRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/search")
public class SearchController {
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final SupplierRepository supplierRepository;

    public SearchController(ProductRepository productRepository, CategoryRepository categoryRepository, SupplierRepository supplierRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.supplierRepository = supplierRepository;
    }

    @GetMapping
    public ResponseEntity<Map<String, Object>> search(@RequestParam String q) {
        String query = q == null ? "" : q.trim();
        Map<String, Object> results = new LinkedHashMap<>();
        if (query.length() < 2) {
            results.put("products", List.of()); results.put("categories", List.of()); results.put("suppliers", List.of());
        } else {
            results.put("products", productRepository.findByNameContainingIgnoreCaseOrSkuContainingIgnoreCase(query, query));
            results.put("categories", categoryRepository.findByNameContainingIgnoreCase(query));
            results.put("suppliers", supplierRepository.findByNameContainingIgnoreCase(query));
        }
        return ResponseEntity.ok(results);
    }
}
