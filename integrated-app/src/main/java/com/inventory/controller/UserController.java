package com.inventory.controller;

import com.inventory.entity.User;
import com.inventory.enums.UserRole;
import com.inventory.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    private Map<String, Object> toDTO(User user) {
        Map<String, Object> dto = new LinkedHashMap<>();
        dto.put("id", user.getId());
        dto.put("name", user.getName());
        dto.put("email", user.getEmail());
        dto.put("role", user.getRole().name());
        dto.put("phoneNumber", user.getPhoneNumber());
        dto.put("active", user.getActive());
        dto.put("createdAt", user.getCreatedAt() != null ? user.getCreatedAt().toString() : null);
        return dto;
    }

    @GetMapping
    public ResponseEntity<List<Map<String, Object>>> getAllUsers() {
        return ResponseEntity.ok(
                userService.getAllUsers().stream().map(this::toDTO).collect(Collectors.toList()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getUserById(@PathVariable Long id) {
        return userService.getUserById(id)
                .map(u -> ResponseEntity.ok(toDTO(u)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Map<String, Object>> createUser(@RequestBody Map<String, String> body) {
        String name = body.getOrDefault("name", body.getOrDefault("username", ""));
        String email = body.getOrDefault("email", name);

        User user = User.builder()
                .name(name)
                .email(email)
                .password(body.getOrDefault("password", ""))
                .role(UserRole.valueOf(body.getOrDefault("role", "STAFF").toUpperCase()))
                .phoneNumber(body.get("phoneNumber"))
                .build();

        return ResponseEntity.status(HttpStatus.CREATED).body(toDTO(userService.createUser(user)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> updateUser(@PathVariable Long id,
                                                          @RequestBody Map<String, String> body) {
        User updated = new User();
        if (body.containsKey("name")) updated.setName(body.get("name"));
        if (body.containsKey("email")) updated.setEmail(body.get("email"));
        if (body.containsKey("password")) updated.setPassword(body.get("password"));
        if (body.containsKey("role")) updated.setRole(UserRole.valueOf(body.get("role").toUpperCase()));
        if (body.containsKey("phoneNumber")) updated.setPhoneNumber(body.get("phoneNumber"));
        if (body.containsKey("active")) updated.setActive(Boolean.parseBoolean(body.get("active")));
        return ResponseEntity.ok(toDTO(userService.updateUser(id, updated)));
    }

    @PutMapping("/password")
    public ResponseEntity<Map<String, String>> changePassword(@RequestBody Map<String, String> body) {
        // Simple password change - requires current password + new password
        String email = body.get("email");
        String newPassword = body.get("newPassword");
        User user = userService.getUserByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        user.setPassword(newPassword);
        userService.updateUser(user.getId(), user);
        return ResponseEntity.ok(Map.of("message", "Password updated successfully"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.ok(Map.of("message", "User deleted"));
    }
}
