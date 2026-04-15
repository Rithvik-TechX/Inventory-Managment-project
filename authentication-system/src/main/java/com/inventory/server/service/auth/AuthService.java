package com.inventory.server.service.auth;

import com.inventory.server.dto.auth.LoginRequestDTO;
import com.inventory.server.dto.auth.LoginResponseDTO;
import com.inventory.server.dto.auth.SignupRequestDTO;
import com.inventory.server.dto.auth.SignupResponseDTO;
import com.inventory.server.entity.Role;
import com.inventory.server.entity.User;
import com.inventory.server.repository.UserRepository;
import com.inventory.server.utils.AuthUtil;
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

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequestDTO.getUsername(), loginRequestDTO.getPassword())
        );

        User user = (User) authentication.getPrincipal();
        String token = authUtil.generateAcessToken(user);
        return new LoginResponseDTO(token, user.getId(), user.getUsername(), user.getRole().name());

    }

    public SignupResponseDTO signup(SignupRequestDTO signupRequestDTO) {
        User user = userRepository.findByUsername(signupRequestDTO.getUsername()).orElse(null);
        if (user != null) {
            throw new IllegalArgumentException("User already exists");
        }

        Role requestedRole = Role.valueOf(signupRequestDTO.getRole().trim().toUpperCase());
        user = userRepository.save(
                User.builder()
                        .username(signupRequestDTO.getUsername())
                        .password(passwordEncoder.encode(signupRequestDTO.getPassword()))
                        .role(requestedRole)
                        .build()
        );
        return new SignupResponseDTO(user.getId(), user.getUsername(), user.getRole().name());
    }
}
