package com.inventory.database_system.dto;

import jakarta.validation.constraints.*;
import lombok.*;
import java.time.LocalDateTime;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class SupplierDTO {
    private Long id;
    @NotBlank @Size(max = 150) private String name;
    @Email private String contactEmail;
    @Size(max = 20) private String contactPhone;
    @Size(max = 255) private String contactInfo;
    @Size(max = 500) private String address;
    private boolean active;
    private LocalDateTime createdAt;
}
