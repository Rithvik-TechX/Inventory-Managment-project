package com.inventory.service;

import com.inventory.entity.PasswordResetToken;
import com.inventory.entity.User;
import com.inventory.repository.PasswordResetTokenRepository;
import com.inventory.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Locale;
import java.util.UUID;

@Service
public class PasswordResetService {
    private final UserRepository userRepository;
    private final PasswordResetTokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;
    private final String frontendUrl;

    public PasswordResetService(
            UserRepository userRepository,
            PasswordResetTokenRepository tokenRepository,
            PasswordEncoder passwordEncoder,
            EmailService emailService,
            @Value("${app.frontend.url:http://localhost:3000}") String frontendUrl) {
        this.userRepository = userRepository;
        this.tokenRepository = tokenRepository;
        this.passwordEncoder = passwordEncoder;
        this.emailService = emailService;
        this.frontendUrl = frontendUrl;
    }

    @Transactional
    public void requestReset(String rawEmail) {
        if (rawEmail == null || rawEmail.isBlank()) return;
        String email = rawEmail.trim().toLowerCase(Locale.ROOT);
        userRepository.findByEmail(email).ifPresent(user -> {
            PasswordResetToken resetToken = new PasswordResetToken();
            resetToken.setToken(UUID.randomUUID().toString());
            resetToken.setUserEmail(user.getEmail());
            resetToken.setExpiryDate(LocalDateTime.now().plusMinutes(30));
            resetToken.setUsed(false);
            tokenRepository.save(resetToken);
            emailService.sendPasswordResetEmail(user.getEmail(), resetToken.getToken(), frontendUrl);
        });
    }

    @Transactional
    public void resetPassword(String token, String newPassword) {
        if (token == null || token.isBlank()) throw new IllegalArgumentException("Invalid or expired reset link");
        validatePassword(newPassword);

        PasswordResetToken resetToken = tokenRepository.findByToken(token)
                .orElseThrow(() -> new IllegalArgumentException("Invalid or expired reset link"));
        if (resetToken.isUsed() || !resetToken.getExpiryDate().isAfter(LocalDateTime.now())) {
            throw new IllegalArgumentException("This reset link has expired. Please request a new one.");
        }

        User user = userRepository.findByEmail(resetToken.getUserEmail())
                .orElseThrow(() -> new IllegalArgumentException("Invalid or expired reset link"));
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
        resetToken.setUsed(true);
        tokenRepository.save(resetToken);
    }

    private void validatePassword(String password) {
        if (password == null || password.length() < 8
                || !password.matches(".*[A-Z].*")
                || !password.matches(".*[a-z].*")
                || !password.matches(".*\\d.*")) {
            throw new IllegalArgumentException("Password must be at least 8 characters and include uppercase, lowercase, and a number.");
        }
    }
}
