package com.turkcell.spring_cqrs.application.features.user.rule;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.turkcell.spring_cqrs.domain.User;
import com.turkcell.spring_cqrs.persistence.repository.UserRepository;

@Component
public class UserBusinessRules {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserBusinessRules(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void userWithSameEmailMustNotExist(String email)
    {
        //...
    }

    public User userWithEmailMustExist(String email)
    {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Invalid credentials"));
    }

    public void userPasswordMustMatch(String rawPassword, String encodedPassword)
    {
        if (!passwordEncoder.matches(rawPassword, encodedPassword))
            throw new RuntimeException("Invalid credentials");
    }
}
