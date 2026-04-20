package com.inventory.repository;

import com.inventory.entity.User;
import com.inventory.enums.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    Optional<User> findByName(String name);

    boolean existsByEmail(String email);

    List<User> findByRole(UserRole role);

    List<User> findByActiveTrue();

    List<User> findByNameContainingIgnoreCase(String name);
}
