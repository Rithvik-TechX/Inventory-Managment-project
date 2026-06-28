package com.inventory;

import com.inventory.entity.User;
import com.inventory.enums.UserRole;
import com.inventory.repository.CategoryRepository;
import com.inventory.repository.ProductRepository;
import com.inventory.repository.SupplierRepository;
import com.inventory.repository.TransactionRepository;
import com.inventory.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class DataLoaderTest {

    private final UserRepository userRepository = mock(UserRepository.class);
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    private final DataLoader dataLoader = new DataLoader(
            userRepository,
            mock(CategoryRepository.class),
            mock(SupplierRepository.class),
            mock(ProductRepository.class),
            mock(TransactionRepository.class),
            passwordEncoder
    );

    @Test
    void createsBcryptAdminWhenDatabaseHasOtherUsersButNoDefaultAdmin() {
        when(userRepository.count()).thenReturn(1L);
        when(userRepository.existsByEmail(DataLoader.DEFAULT_ADMIN_EMAIL)).thenReturn(false);

        dataLoader.run();

        verify(userRepository).save(org.mockito.ArgumentMatchers.argThat(user ->
                DataLoader.DEFAULT_ADMIN_EMAIL.equals(user.getEmail())
                        && user.getRole() == UserRole.ADMIN
                        && passwordEncoder.matches(DataLoader.DEFAULT_ADMIN_PASSWORD, user.getPassword())
        ));
    }

    @Test
    void doesNotDuplicateOrOverwriteExistingDefaultAdmin() {
        when(userRepository.count()).thenReturn(1L);
        when(userRepository.existsByEmail(DataLoader.DEFAULT_ADMIN_EMAIL)).thenReturn(true);

        dataLoader.run();

        verify(userRepository, never()).save(any(User.class));
    }
}
