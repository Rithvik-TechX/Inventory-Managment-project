package com.inventory.database_system.dto;

import com.inventory.database_system.entity.User;
import jakarta.validation.constraints.*;
import lombok.*;
import java.time.LocalDateTime;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class UserDTO {
    private Long id;
    @NotBlank @Size(max = 100) private String name;
    @NotBlank @Email private String email;
    @Size(min = 6) private String password;
    @Size(max = 20) private String phoneNumber;
    private User.Role role;
    private boolean active;
    private LocalDateTime createdAt;
}
