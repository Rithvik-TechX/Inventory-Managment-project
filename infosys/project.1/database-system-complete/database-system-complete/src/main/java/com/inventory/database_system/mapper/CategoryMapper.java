package com.inventory.database_system.mapper;

import com.inventory.database_system.dto.CategoryDTO;
import com.inventory.database_system.entity.Category;
import org.springframework.stereotype.Component;

@Component
public class CategoryMapper {

    public CategoryDTO toDTO(Category c) {
        if (c == null) return null;
        return CategoryDTO.builder()
                .id(c.getId())
                .name(c.getName())
                .description(c.getDescription())
                .active(c.isActive())
                .productCount(c.getProducts() != null ? c.getProducts().size() : 0)
                .build();
    }

    public Category toEntity(CategoryDTO dto) {
        if (dto == null) return null;
        return Category.builder()
                .id(dto.getId())
                .name(dto.getName())
                .description(dto.getDescription())
                .active(dto.isActive())
                .build();
    }
}
