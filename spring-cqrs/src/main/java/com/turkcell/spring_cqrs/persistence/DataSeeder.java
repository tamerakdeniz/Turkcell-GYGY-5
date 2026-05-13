package com.turkcell.spring_cqrs.persistence;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.turkcell.spring_cqrs.domain.User;
import com.turkcell.spring_cqrs.persistence.repository.UserRepository;

@Component
public class DataSeeder implements CommandLineRunner {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public DataSeeder(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        seedUser("user@test.com", "user123", "USER");
        seedUser("admin@test.com", "admin123", "ADMIN");
    }

    private void seedUser(String email, String password, String role) {
        userRepository.findByEmail(email).ifPresentOrElse(
            user -> {
                if (user.getRole() == null || user.getRole().isBlank()) {
                    user.setRole(role);
                    userRepository.save(user);
                }
            },
            () -> {
                User user = new User();
                user.setEmail(email);
                user.setPassword(passwordEncoder.encode(password));
                user.setRole(role);
                userRepository.save(user);
            }
        );
    }
}
