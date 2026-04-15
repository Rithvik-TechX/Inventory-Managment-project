package com.inventory.service.auth;

import com.inventory.dto.auth.LoginRequestDTO;
import com.inventory.dto.auth.LoginResponseDTO;
import com.inventory.dto.auth.SignupRequestDTO;
import com.inventory.dto.auth.SignupResponseDTO;
import com.inventory.entity.User;
import com.inventory.enums.UserRole;
import com.inventory.repository.UserRepository;
import com.inventory.security.AuthUtil;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final AuthUtil authUtil;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthService(
            AuthenticationManager authenticationManager,
            AuthUtil authUtil,
            UserRepository userRepository,
            PasswordEncoder passwordEncoder
    ) {
        this.authenticationManager = authenticationManager;
        this.authUtil = authUtil;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public LoginResponseDTO login(LoginRequestDTO loginRequestDTO) {
        // The username field from frontend contains the email
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequestDTO.getUsername(),
                        loginRequestDTO.getPassword())
        );

        User user = (User) authentication.getPrincipal();
        String token = authUtil.generateAccessToken(user);
        return new LoginResponseDTO(token, user.getId(), user.getName(), user.getRole().name());
    }

    public SignupResponseDTO signup(SignupRequestDTO signupRequestDTO) {
        // Check if user already exists by email
        String email = signupRequestDTO.getEmail() != null
                ? signupRequestDTO.getEmail()
                : signupRequestDTO.getUsername();

        if (userRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("User with email '" + email + "' already exists");
        }

        UserRole requestedRole = UserRole.valueOf(signupRequestDTO.getRole().trim().toUpperCase());
        String name = signupRequestDTO.getName() != null
                ? signupRequestDTO.getName()
                : signupRequestDTO.getUsername();

        User user = userRepository.save(
                User.builder()
                        .name(name)
                        .email(email)
                        .password(passwordEncoder.encode(signupRequestDTO.getPassword()))
                        .role(requestedRole)
                        .build()
        );
        return new SignupResponseDTO(user.getId(), user.getName(), user.getRole().name());
    }
}
