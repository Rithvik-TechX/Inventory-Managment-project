package com.inventory.database_system.controller;

import com.inventory.database_system.dto.ApiResponse;
import com.inventory.database_system.dto.ProductDTO;
import com.inventory.database_system.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<ProductDTO>>> getAll() {
        return ResponseEntity.ok(ApiResponse.success(productService.getActiveProducts(), "Products fetched"));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ProductDTO>> getById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(productService.getProductById(id), "Product found"));
    }

    @GetMapping("/sku/{sku}")
    public ResponseEntity<ApiResponse<ProductDTO>> getBySku(@PathVariable String sku) {
        return ResponseEntity.ok(ApiResponse.success(productService.getProductBySku(sku), "Product found"));
    }

    @GetMapping("/low-stock")
    public ResponseEntity<ApiResponse<List<ProductDTO>>> getLowStock() {
        return ResponseEntity.ok(ApiResponse.success(productService.getLowStockProducts(), "Low stock products"));
    }

    @GetMapping("/out-of-stock")
    public ResponseEntity<ApiResponse<List<ProductDTO>>> getOutOfStock() {
        return ResponseEntity.ok(ApiResponse.success(productService.getOutOfStockProducts(), "Out of stock products"));
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponse<List<ProductDTO>>> search(@RequestParam String keyword) {
        return ResponseEntity.ok(ApiResponse.success(productService.searchProducts(keyword), "Search results"));
    }

    @GetMapping("/category/{categoryId}")
    public ResponseEntity<ApiResponse<List<ProductDTO>>> getByCategory(@PathVariable Long categoryId) {
        return ResponseEntity.ok(ApiResponse.success(productService.getProductsByCategory(categoryId), "Products by category"));
    }

    @GetMapping("/supplier/{supplierId}")
    public ResponseEntity<ApiResponse<List<ProductDTO>>> getBySupplier(@PathVariable Long supplierId) {
        return ResponseEntity.ok(ApiResponse.success(productService.getProductsBySupplier(supplierId), "Products by supplier"));
    }

    @GetMapping("/price-range")
    public ResponseEntity<ApiResponse<List<ProductDTO>>> getByPriceRange(
            @RequestParam BigDecimal min, @RequestParam BigDecimal max) {
        return ResponseEntity.ok(ApiResponse.success(productService.getProductsByPriceRange(min, max), "Products in range"));
    }

    @GetMapping("/inventory-value")
    public ResponseEntity<ApiResponse<BigDecimal>> getInventoryValue() {
        return ResponseEntity.ok(ApiResponse.success(productService.getTotalInventoryValue(), "Total inventory value"));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<ProductDTO>> create(@Valid @RequestBody ProductDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(productService.createProduct(dto), "Product created"));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<ProductDTO>> update(@PathVariable Long id, @Valid @RequestBody ProductDTO dto) {
        return ResponseEntity.ok(ApiResponse.success(productService.updateProduct(id, dto), "Product updated"));
    }

    @PatchMapping("/{id}/deactivate")
    public ResponseEntity<ApiResponse<Void>> deactivate(@PathVariable Long id) {
        productService.deactivateProduct(id);
        return ResponseEntity.ok(ApiResponse.success(null, "Product deactivated"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.ok(ApiResponse.success(null, "Product deleted"));
    }
}
