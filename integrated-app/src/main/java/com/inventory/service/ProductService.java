package com.inventory.service;

import com.inventory.entity.Product;
import com.inventory.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    //  CREATE
    public Product createProduct(Product product) {
        if (product.getSku() != null && productRepository.existsBySku(product.getSku())) {
            throw new RuntimeException("Product with SKU '" + product.getSku() + "' already exists");
        }
        return productRepository.save(product);
    }

    //  READ
    @Transactional(readOnly = true)
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<Product> getActiveProducts() {
        return productRepository.findByActiveTrue();
    }

    @Transactional(readOnly = true)
    public Optional<Product> getProductById(Long id) {
        return productRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public Optional<Product> getProductBySku(String sku) {
        return productRepository.findBySku(sku);
    }

    //  SEARCH & FILTER
    @Transactional(readOnly = true)
    public List<Product> searchProducts(String name) {
        return productRepository.findByNameContainingIgnoreCase(name);
    }

    @Transactional(readOnly = true)
    public List<Product> getProductsByCategory(Long categoryId) {
        return productRepository.findByCategoryId(categoryId);
    }

    @Transactional(readOnly = true)
    public List<Product> getProductsBySupplier(Long supplierId) {
        return productRepository.findBySupplierId(supplierId);
    }

    //  STOCK MANAGEMENT
    @Transactional(readOnly = true)
    public List<Product> getLowStockProducts(int threshold) {
        return productRepository.findByQuantityLessThanEqual(threshold);
    }

    public Product updateStock(Long productId, int quantityChange) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + productId));

        int newQuantity = product.getQuantity() + quantityChange;

        if (newQuantity < 0) {
            throw new RuntimeException("Insufficient stock. Available: "
                    + product.getQuantity() + ", Requested change: " + quantityChange);
        }

        product.setQuantity(newQuantity);
        return productRepository.save(product);
    }

    //  UPDATE
    public Product updateProduct(Long id, Product updated) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + id));

        if (updated.getName() != null) product.setName(updated.getName());
        if (updated.getSku() != null) product.setSku(updated.getSku());
        if (updated.getDescription() != null) product.setDescription(updated.getDescription());
        if (updated.getPrice() != null) product.setPrice(updated.getPrice());

        product.setQuantity(updated.getQuantity());

        if (updated.getReorderLevel() != null) product.setReorderLevel(updated.getReorderLevel());
        if (updated.getCategory() != null) product.setCategory(updated.getCategory());
        if (updated.getSupplier() != null) product.setSupplier(updated.getSupplier());
        if (updated.getActive() != null) product.setActive(updated.getActive());

        return productRepository.save(product);
    }

    //  COUNT
    @Transactional(readOnly = true)
    public long getProductCount() {
        return productRepository.count();
    }

    //  SOFT DELETE
    public Product deactivateProduct(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + id));

        product.setActive(false);
        return productRepository.save(product);
    }

    //  HARD DELETE
    public void deleteProduct(Long id) {
        if (!productRepository.existsById(id)) {
            throw new RuntimeException("Product not found with id: " + id);
        }
        productRepository.deleteById(id);
    }
}
