package com.inventory.database_system.mapper;

import com.inventory.database_system.dto.UserDTO;
import com.inventory.database_system.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public UserDTO toDTO(User u) {
        if (u == null) return null;
        return UserDTO.builder()
                .id(u.getId())
                .name(u.getName())
                .email(u.getEmail())
                // Never expose password
                .phoneNumber(u.getPhoneNumber())
                .role(u.getRole())
                .active(u.isActive())
                .createdAt(u.getCreatedAt())
                .build();
    }

    public User toEntity(UserDTO dto) {
        if (dto == null) return null;
        return User.builder()
                .id(dto.getId())
                .name(dto.getName())
                .email(dto.getEmail())
                .password(dto.getPassword())
                .phoneNumber(dto.getPhoneNumber())
                .role(dto.getRole() != null ? dto.getRole() : User.Role.STAFF)
                .active(dto.isActive())
                .build();
    }
}
