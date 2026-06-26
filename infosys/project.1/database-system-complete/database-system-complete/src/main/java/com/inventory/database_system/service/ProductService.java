package com.inventory.database_system.service;

import com.inventory.database_system.dto.ProductDTO;
import com.inventory.database_system.entity.Category;
import com.inventory.database_system.entity.Product;
import com.inventory.database_system.entity.Supplier;
import com.inventory.database_system.exception.DuplicateResourceException;
import com.inventory.database_system.exception.ResourceNotFoundException;
import com.inventory.database_system.mapper.ProductMapper;
import com.inventory.database_system.repository.CategoryRepository;
import com.inventory.database_system.repository.ProductRepository;
import com.inventory.database_system.repository.SupplierRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final SupplierRepository supplierRepository;
    private final ProductMapper productMapper;

    public List<ProductDTO> getAllProducts() {
        return productRepository.findAll().stream()
                .map(productMapper::toDTO).collect(Collectors.toList());
    }

    public List<ProductDTO> getActiveProducts() {
        return productRepository.findByActiveTrue().stream()
                .map(productMapper::toDTO).collect(Collectors.toList());
    }

    public ProductDTO getProductById(Long id) {
        Product product = productRepository.findByIdWithDetails(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "id", id));
        return productMapper.toDTO(product);
    }

    public ProductDTO getProductBySku(String sku) {
        Product product = productRepository.findBySku(sku)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "sku", sku));
        return productMapper.toDTO(product);
    }

    public List<ProductDTO> getLowStockProducts() {
        return productRepository.findLowStockProducts().stream()
                .map(productMapper::toDTO).collect(Collectors.toList());
    }

    public List<ProductDTO> getOutOfStockProducts() {
        return productRepository.findOutOfStockProducts().stream()
                .map(productMapper::toDTO).collect(Collectors.toList());
    }

    public List<ProductDTO> searchProducts(String keyword) {
        return productRepository.searchProducts(keyword).stream()
                .map(productMapper::toDTO).collect(Collectors.toList());
    }

    public List<ProductDTO> getProductsByCategory(Long categoryId) {
        return productRepository.findByCategories_Id(categoryId).stream()
                .map(productMapper::toDTO).collect(Collectors.toList());
    }

    public List<ProductDTO> getProductsBySupplier(Long supplierId) {
        return productRepository.findBySupplier_Id(supplierId).stream()
                .map(productMapper::toDTO).collect(Collectors.toList());
    }

    public List<ProductDTO> getProductsByPriceRange(BigDecimal min, BigDecimal max) {
        return productRepository.findByPriceRange(min, max).stream()
                .map(productMapper::toDTO).collect(Collectors.toList());
    }

    public BigDecimal getTotalInventoryValue() {
        BigDecimal value = productRepository.calculateTotalInventoryValue();
        return value != null ? value : BigDecimal.ZERO;
    }

    @Transactional
    public ProductDTO createProduct(ProductDTO dto) {
        if (productRepository.existsBySku(dto.getSku())) {
            throw new DuplicateResourceException("Product with SKU '" + dto.getSku() + "' already exists");
        }
        Product product = productMapper.toEntity(dto);

        if (dto.getCategoryId() != null) {
            Category category = categoryRepository.findById(dto.getCategoryId())
                    .orElseThrow(() -> new ResourceNotFoundException("Category", "id", dto.getCategoryId()));
            product.setCategories(category);
        }
        if (dto.getSupplierId() != null) {
            Supplier supplier = supplierRepository.findById(dto.getSupplierId())
                    .orElseThrow(() -> new ResourceNotFoundException("Supplier", "id", dto.getSupplierId()));
            product.setSupplier(supplier);
        }
        product.setActive(true);
        return productMapper.toDTO(productRepository.save(product));
    }

    @Transactional
    public ProductDTO updateProduct(Long id, ProductDTO dto) {
        Product existing = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "id", id));

        if (!existing.getSku().equals(dto.getSku()) && productRepository.existsBySku(dto.getSku())) {
            throw new DuplicateResourceException("SKU '" + dto.getSku() + "' already in use");
        }

        existing.setName(dto.getName());
        existing.setDescription(dto.getDescription());
        existing.setSku(dto.getSku());
        existing.setPrice(dto.getPrice());
        existing.setQuantity(dto.getQuantity());
        existing.setReorderLevel(dto.getReorderLevel());
        existing.setActive(dto.isActive());

        if (dto.getCategoryId() != null) {
            Category category = categoryRepository.findById(dto.getCategoryId())
                    .orElseThrow(() -> new ResourceNotFoundException("Category", "id", dto.getCategoryId()));
            existing.setCategories(category);
        }
        if (dto.getSupplierId() != null) {
            Supplier supplier = supplierRepository.findById(dto.getSupplierId())
                    .orElseThrow(() -> new ResourceNotFoundException("Supplier", "id", dto.getSupplierId()));
            existing.setSupplier(supplier);
        }
        return productMapper.toDTO(productRepository.save(existing));
    }

    @Transactional
    public void deleteProduct(Long id) {
        if (!productRepository.existsById(id)) {
            throw new ResourceNotFoundException("Product", "id", id);
        }
        productRepository.deleteById(id);
    }

    @Transactional
    public void deactivateProduct(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "id", id));
        product.setActive(false);
        productRepository.save(product);
    }
}
