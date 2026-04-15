package com.inventory.service;

import com.inventory.entity.User;
import com.inventory.enums.UserRole;
import com.inventory.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User createUser(User user) {
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new RuntimeException("User with email '" + user.getEmail() + "' already exists");
        }
        // Encode password before saving
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    @Transactional(readOnly = true)
    public List<User> getAllUsers() { return userRepository.findAll(); }

    @Transactional(readOnly = true)
    public List<User> getActiveUsers() { return userRepository.findByActiveTrue(); }

    @Transactional(readOnly = true)
    public Optional<User> getUserById(Long id) { return userRepository.findById(id); }

    @Transactional(readOnly = true)
    public Optional<User> getUserByEmail(String email) { return userRepository.findByEmail(email); }

    @Transactional(readOnly = true)
    public List<User> getUsersByRole(UserRole role) { return userRepository.findByRole(role); }

    @Transactional(readOnly = true)
    public List<User> searchUsers(String name) {
        return userRepository.findByNameContainingIgnoreCase(name);
    }

    public User updateUser(Long id, User updated) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
        if (updated.getName() != null) user.setName(updated.getName());
        if (updated.getEmail() != null) user.setEmail(updated.getEmail());
        if (updated.getPassword() != null && !updated.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(updated.getPassword()));
        }
        if (updated.getPhoneNumber() != null) user.setPhoneNumber(updated.getPhoneNumber());
        if (updated.getRole() != null) user.setRole(updated.getRole());
        if (updated.getActive() != null) user.setActive(updated.getActive());
        return userRepository.save(user);
    }

    @Transactional(readOnly = true)
    public boolean emailExists(String email) { return userRepository.existsByEmail(email); }

    public User deactivateUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
        user.setActive(false);
        return userRepository.save(user);
    }

    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new RuntimeException("User not found with id: " + id);
        }
        userRepository.deleteById(id);
    }
}
