package com.turkcell.spring_starter.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.turkcell.spring_starter.dto.RegisterRequest;
import com.turkcell.spring_starter.entity.User;
import com.turkcell.spring_starter.repository.UserRepository;

@Service
public class UserServiceImpl {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void registerUser(RegisterRequest registerRequest) {
        
        User user = new User();
        user.setEmail(registerRequest.getEmail());

        String encodedPassword = this.passwordEncoder.encode(registerRequest.getPassword()); // Şifreleme işlemi burada yapılmalı (örneğin, BCrypt ile)
        user.setPassword(encodedPassword); // PLAIN - düz metin olarak kaydedilir, gerçek uygulamalarda şifreler hash'lenmelidir.

        userRepository.save(user);
        }
}
