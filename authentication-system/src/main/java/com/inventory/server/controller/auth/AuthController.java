package com.inventory.server.controller.auth;

import com.inventory.server.dto.auth.LoginRequestDTO;
import com.inventory.server.dto.auth.LoginResponseDTO;
import com.inventory.server.dto.auth.SignupRequestDTO;
import com.inventory.server.dto.auth.SignupResponseDTO;
import com.inventory.server.service.auth.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody LoginRequestDTO loginRequestDTO) {
        return ResponseEntity.ok(authService.login(loginRequestDTO));
    }

    @PostMapping("/signup")
    public ResponseEntity<SignupResponseDTO> signup(@RequestBody SignupRequestDTO signupRequestDTO) {
        return ResponseEntity.ok(authService.signup(signupRequestDTO));
    }
}
