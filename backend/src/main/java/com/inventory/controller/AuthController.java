package com.inventory.controller;

import com.inventory.dto.auth.LoginRequestDTO;
import com.inventory.dto.auth.LoginResponseDTO;
import com.inventory.dto.auth.SignupRequestDTO;
import com.inventory.dto.auth.SignupResponseDTO;
import com.inventory.service.auth.AuthService;
import com.inventory.service.UserService;
import com.inventory.entity.User;
import com.inventory.enums.UserRole;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;
    private final UserService userService;

    public AuthController(AuthService authService, UserService userService) {
        this.authService = authService;
        this.userService = userService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequestDTO loginRequestDTO) {
        try {
            LoginResponseDTO response = authService.login(loginRequestDTO);
            return ResponseEntity.ok(response);
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Invalid username or password"));
        }
    }

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody SignupRequestDTO signupRequestDTO) {
        try {
            SignupResponseDTO response = authService.signup(signupRequestDTO);
            return ResponseEntity.ok(response);
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", ex.getMessage()));
        }
    }

    @GetMapping("/health")
    public ResponseEntity<Map<String, String>> health() {
        return ResponseEntity.ok(Map.of("status", "ok"));
    }

    @PostMapping("/create-user")
    public ResponseEntity<?> createUser(@RequestBody Map<String,String> body) {
        String name = (body.getOrDefault("firstName", "") + " " + body.getOrDefault("lastName", "")).trim();
        User created = userService.createUser(User.builder().name(name).email(body.get("email"))
                .password(body.get("password")).role(UserRole.valueOf(body.get("role").toUpperCase())).build());
        return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("id", created.getId(), "name", created.getName(),
                "email", created.getEmail(), "role", created.getRole().name(), "active", created.getActive()));
    }
}
