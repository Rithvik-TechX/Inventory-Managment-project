package com.inventory.database_system.mapper;

import com.inventory.database_system.dto.SupplierDTO;
import com.inventory.database_system.entity.Supplier;
import org.springframework.stereotype.Component;

@Component
public class SupplierMapper {

    public SupplierDTO toDTO(Supplier s) {
        if (s == null) return null;
        return SupplierDTO.builder()
                .id(s.getId())
                .name(s.getName())
                .contactEmail(s.getContactEmail())
                .contactPhone(s.getContactPhone())
                .contactInfo(s.getContactInfo())
                .address(s.getAddress())
                .active(s.isActive())
                .createdAt(s.getCreatedAt())
                .build();
    }

    public Supplier toEntity(SupplierDTO dto) {
        if (dto == null) return null;
        return Supplier.builder()
                .id(dto.getId())
                .name(dto.getName())
                .contactEmail(dto.getContactEmail())
                .contactPhone(dto.getContactPhone())
                .contactInfo(dto.getContactInfo())
                .address(dto.getAddress())
                .active(dto.isActive())
                .build();
    }
}
