package com.turkcell.spring_starter.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.turkcell.spring_starter.dto.LoginRequest;
import com.turkcell.spring_starter.dto.RegisterRequest;
import com.turkcell.spring_starter.entity.User;
import com.turkcell.spring_starter.exception.InvalidCredentialsException;
import com.turkcell.spring_starter.exception.UserAlreadyExistsException;
import com.turkcell.spring_starter.repository.UserRepository;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public String registerUser(RegisterRequest request) {
        userRepository.findByEmail(request.email()).ifPresent(existing -> {
            throw new UserAlreadyExistsException("Bu e-posta zaten kayıtlı");
        });

        User user = new User();
        user.setEmail(request.email());
        user.setPassword(passwordEncoder.encode(request.password()));

        userRepository.save(user);

        return "Kayıt başarılı";
    }

    @Override
    public String login(LoginRequest request) {
        String errorMessage = "Giriş bilgileri yanlış";

        User user = userRepository.findByEmail(request.email())
                .orElseThrow(() -> new InvalidCredentialsException(errorMessage));

        boolean passwordMatch = passwordEncoder.matches(request.password(), user.getPassword());
        if (!passwordMatch) {
            throw new InvalidCredentialsException(errorMessage);
        }

        return "Giriş başarılı";
    }
}
