package com.inventory;

import com.inventory.entity.PasswordResetToken;
import com.inventory.entity.User;
import com.inventory.enums.UserRole;
import com.inventory.repository.PasswordResetTokenRepository;
import com.inventory.repository.UserRepository;
import com.inventory.service.EmailService;
import com.inventory.service.PasswordResetService;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class PasswordResetServiceTest {
    private final UserRepository users = mock(UserRepository.class);
    private final PasswordResetTokenRepository tokens = mock(PasswordResetTokenRepository.class);
    private final EmailService email = mock(EmailService.class);
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
    private final PasswordResetService service = new PasswordResetService(users, tokens, encoder, email, "http://localhost:3000");

    @Test
    void createsExpiringTokenAndSendsResetEmailForKnownAccount() {
        User user = user("person@example.com", "OldPassword1");
        when(users.findByEmail("person@example.com")).thenReturn(Optional.of(user));
        when(tokens.save(any(PasswordResetToken.class))).thenAnswer(invocation -> invocation.getArgument(0));

        service.requestReset(" Person@Example.com ");

        verify(tokens).save(any(PasswordResetToken.class));
        verify(email).sendPasswordResetEmail(org.mockito.ArgumentMatchers.eq("person@example.com"), any(String.class), org.mockito.ArgumentMatchers.eq("http://localhost:3000"));
    }

    @Test
    void unknownAccountKeepsResponsePathSilent() {
        when(users.findByEmail("missing@example.com")).thenReturn(Optional.empty());
        service.requestReset("missing@example.com");
        verify(tokens, never()).save(any());
        verify(email, never()).sendPasswordResetEmail(any(), any(), any());
    }

    @Test
    void validTokenChangesPasswordAndCannotBeReused() {
        User user = user("person@example.com", "OldPassword1");
        PasswordResetToken token = token(false, LocalDateTime.now().plusMinutes(10));
        when(tokens.findByToken("valid-token")).thenReturn(Optional.of(token));
        when(users.findByEmail(user.getEmail())).thenReturn(Optional.of(user));

        service.resetPassword("valid-token", "NewPassword2");

        assertThat(encoder.matches("NewPassword2", user.getPassword())).isTrue();
        assertThat(token.isUsed()).isTrue();
        verify(users).save(user);
        verify(tokens).save(token);
        assertThatThrownBy(() -> service.resetPassword("valid-token", "AnotherPassword3"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("expired");
    }

    @Test
    void expiredTokenIsRejectedWithoutChangingPassword() {
        PasswordResetToken token = token(false, LocalDateTime.now().minusSeconds(1));
        when(tokens.findByToken("valid-token")).thenReturn(Optional.of(token));
        assertThatThrownBy(() -> service.resetPassword("valid-token", "NewPassword2"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("expired");
        verify(users, never()).save(any());
    }

    private User user(String emailAddress, String rawPassword) {
        return User.builder().name("Test User").email(emailAddress).password(encoder.encode(rawPassword)).role(UserRole.STAFF).build();
    }

    private PasswordResetToken token(boolean used, LocalDateTime expiry) {
        PasswordResetToken resetToken = new PasswordResetToken();
        resetToken.setToken("valid-token");
        resetToken.setUserEmail("person@example.com");
        resetToken.setExpiryDate(expiry);
        resetToken.setUsed(used);
        return resetToken;
    }
}
