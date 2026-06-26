package com.inventory.database_system.mapper;

import com.inventory.database_system.dto.ProductDTO;
import com.inventory.database_system.entity.Product;
import org.springframework.stereotype.Component;

@Component
public class ProductMapper {

    public ProductDTO toDTO(Product p) {
        if (p == null) return null;
        return ProductDTO.builder()
                .id(p.getId())
                .name(p.getName())
                .description(p.getDescription())
                .sku(p.getSku())
                .price(p.getPrice())
                .quantity(p.getQuantity())
                .reorderLevel(p.getReorderLevel())
                .active(p.isActive())
                .lowStock(p.isLowStock())
                .categoryId(p.getCategories() != null ? p.getCategories().getId() : null)
                .categoryName(p.getCategories() != null ? p.getCategories().getName() : null)
                .supplierId(p.getSupplier() != null ? p.getSupplier().getId() : null)
                .supplierName(p.getSupplier() != null ? p.getSupplier().getName() : null)
                .createdAt(p.getCreatedAt())
                .updatedAt(p.getUpdatedAt())
                .build();
    }

    public Product toEntity(ProductDTO dto) {
        if (dto == null) return null;
        return Product.builder()
                .id(dto.getId())
                .name(dto.getName())
                .description(dto.getDescription())
                .sku(dto.getSku())
                .price(dto.getPrice())
                .quantity(dto.getQuantity())
                .reorderLevel(dto.getReorderLevel())
                .active(dto.isActive())
                .build();
    }
}
