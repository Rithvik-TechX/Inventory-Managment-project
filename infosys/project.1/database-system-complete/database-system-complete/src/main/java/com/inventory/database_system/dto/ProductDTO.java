package com.inventory.database_system.dto;

import jakarta.validation.constraints.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class ProductDTO {
    private Long id;
    @NotBlank @Size(max = 200) private String name;
    @Size(max = 1000) private String description;
    @NotBlank @Size(max = 50) private String sku;
    @NotNull @DecimalMin("0.01") private BigDecimal price;
    @Min(0) private int quantity;
    private int reorderLevel;
    private boolean active;
    private boolean lowStock;
    private Long categoryId;
    private String categoryName;
    private Long supplierId;
    private String supplierName;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
