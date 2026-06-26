package com.inventory.database_system.dto;

import jakarta.validation.constraints.*;
import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class CategoryDTO {
    private Long id;
    @NotBlank @Size(max = 100) private String name;
    @Size(max = 500) private String description;
    private boolean active;
    private long productCount;
}
