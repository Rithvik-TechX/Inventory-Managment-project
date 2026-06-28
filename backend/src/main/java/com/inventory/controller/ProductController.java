package com.inventory.controller;

import com.inventory.dto.ProductDTO;
import com.inventory.entity.Category;
import com.inventory.entity.Product;
import com.inventory.entity.Supplier;
import com.inventory.repository.CategoryRepository;
import com.inventory.repository.SupplierRepository;
import com.inventory.service.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import com.inventory.entity.User;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;
    private final CategoryRepository categoryRepository;
    private final SupplierRepository supplierRepository;

    public ProductController(ProductService productService,
                             CategoryRepository categoryRepository,
                             SupplierRepository supplierRepository) {
        this.productService = productService;
        this.categoryRepository = categoryRepository;
        this.supplierRepository = supplierRepository;
    }

    // ── Helper: Entity → DTO ───────────────────────────────────────────
    private ProductDTO toDTO(Product p) {
        return ProductDTO.builder()
                .id(p.getId())
                .name(p.getName())
                .sku(p.getSku())
                .description(p.getDescription())
                .price(p.getPrice())
                .quantity(p.getQuantity())
                .maxStock(p.getMaxStock())
                .reorderLevel(p.getReorderLevel())
                .lowStock(p.isLowStock())
                .active(p.getActive())
                .createdAt(p.getCreatedAt() != null ? p.getCreatedAt().toString() : null)
                .updatedAt(p.getUpdatedAt() != null ? p.getUpdatedAt().toString() : null)
                .categoryId(p.getCategory() != null ? p.getCategory().getId() : null)
                .categoryName(p.getCategory() != null ? p.getCategory().getName() : null)
                .supplierId(p.getSupplier() != null ? p.getSupplier().getId() : null)
                .supplierName(p.getSupplier() != null ? p.getSupplier().getName() : null)
                .build();
    }

    // ── Helper: Request body map → Entity ──────────────────────────────
    private Product fromRequest(Map<String, Object> body) {
        Product product = new Product();
        if (body.containsKey("name")) product.setName((String) body.get("name"));
        if (body.containsKey("sku")) product.setSku((String) body.get("sku"));
        if (body.containsKey("description")) product.setDescription((String) body.get("description"));
        if (body.containsKey("price")) product.setPrice(new BigDecimal(String.valueOf(body.get("price"))));
        if (body.containsKey("unitPrice")) product.setPrice(new BigDecimal(String.valueOf(body.get("unitPrice"))));
        if (body.containsKey("quantity")) product.setQuantity(((Number) body.get("quantity")).intValue());
        if (body.containsKey("maxStock")) product.setMaxStock(((Number) body.get("maxStock")).intValue());
        if (body.containsKey("reorderLevel")) product.setReorderLevel(((Number) body.get("reorderLevel")).intValue());
        if (body.containsKey("active")) product.setActive((Boolean) body.get("active"));

        // Handle category
        if (body.containsKey("categoryId")) {
            Long catId = Long.valueOf(String.valueOf(body.get("categoryId")));
            Category cat = categoryRepository.findById(catId).orElse(null);
            product.setCategory(cat);
        } else if (body.containsKey("category")) {
            Object catValue = body.get("category");
            if (catValue instanceof String) {
                categoryRepository.findByName((String) catValue).ifPresent(product::setCategory);
            } else if (catValue instanceof Map) {
                Long catId = Long.valueOf(String.valueOf(((Map<?,?>) catValue).get("id")));
                categoryRepository.findById(catId).ifPresent(product::setCategory);
            }
        }

        // Handle supplier
        if (body.containsKey("supplierId")) {
            Long supId = Long.valueOf(String.valueOf(body.get("supplierId")));
            Supplier sup = supplierRepository.findById(supId).orElse(null);
            product.setSupplier(sup);
        } else if (body.containsKey("supplier")) {
            Object supValue = body.get("supplier");
            if (supValue instanceof String) {
                supplierRepository.findByNameContainingIgnoreCase((String) supValue).stream()
                        .findFirst().ifPresent(product::setSupplier);
            } else if (supValue instanceof Map) {
                Long supId = Long.valueOf(String.valueOf(((Map<?,?>) supValue).get("id")));
                supplierRepository.findById(supId).ifPresent(product::setSupplier);
            }
        }

        return product;
    }

    // ── CRUD Endpoints ─────────────────────────────────────────────────

    @GetMapping
    public ResponseEntity<List<ProductDTO>> getAllProducts() {
        List<ProductDTO> dtos = productService.getAllProducts().stream()
                .map(this::toDTO).collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/active")
    public ResponseEntity<List<ProductDTO>> getActiveProducts() {
        List<ProductDTO> dtos = productService.getActiveProducts().stream()
                .map(this::toDTO).collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDTO> getProductById(@PathVariable Long id) {
        return productService.getProductById(id)
                .map(p -> ResponseEntity.ok(toDTO(p)))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/search")
    public ResponseEntity<List<ProductDTO>> searchProducts(@RequestParam(required = false) String q,
                                                           @RequestParam(required = false) String name) {
        String searchTerm = q != null ? q : name;
        if (searchTerm == null || searchTerm.isBlank()) {
            return ResponseEntity.ok(productService.getAllProducts().stream()
                    .map(this::toDTO).collect(Collectors.toList()));
        }
        List<ProductDTO> dtos = productService.searchProducts(searchTerm).stream()
                .map(this::toDTO).collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/category/{categoryId}")
    public ResponseEntity<List<ProductDTO>> getProductsByCategory(@PathVariable Long categoryId) {
        List<ProductDTO> dtos = productService.getProductsByCategory(categoryId).stream()
                .map(this::toDTO).collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/supplier/{supplierId}")
    public ResponseEntity<List<ProductDTO>> getProductsBySupplier(@PathVariable Long supplierId) {
        List<ProductDTO> dtos = productService.getProductsBySupplier(supplierId).stream()
                .map(this::toDTO).collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/low-stock")
    public ResponseEntity<List<ProductDTO>> getLowStockProducts(
            @RequestParam(defaultValue = "10") int threshold) {
        List<ProductDTO> dtos = productService.getLowStockProducts(threshold).stream()
                .map(this::toDTO).collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    @PostMapping
    public ResponseEntity<ProductDTO> createProduct(@RequestBody Map<String, Object> body,
                                                    @AuthenticationPrincipal User currentUser) {
        Product product = fromRequest(body);
        product.setCreatedBy(currentUser);
        Product saved = productService.createProduct(product);
        return ResponseEntity.status(HttpStatus.CREATED).body(toDTO(saved));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductDTO> updateProduct(@PathVariable Long id,
                                                    @RequestBody Map<String, Object> body) {
        Product product = fromRequest(body);
        Product updated = productService.updateProduct(id, product);
        return ResponseEntity.ok(toDTO(updated));
    }

    @PutMapping("/{id}/restock")
    public ResponseEntity<ProductDTO> restockProduct(@PathVariable Long id,
                                                     @RequestBody Map<String, Object> body) {
        int qty = ((Number) body.get("quantity")).intValue();
        Product updated = productService.updateStock(id, qty);
        return ResponseEntity.ok(toDTO(updated));
    }

    @PatchMapping("/{id}/deactivate")
    public ResponseEntity<ProductDTO> deactivateProduct(@PathVariable Long id) {
        return ResponseEntity.ok(toDTO(productService.deactivateProduct(id)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.ok(Map.of("message", "Product deleted successfully"));
    }
}
