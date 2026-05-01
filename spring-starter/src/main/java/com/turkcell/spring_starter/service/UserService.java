package com.turkcell.spring_starter.service;

import com.turkcell.spring_starter.dto.LoginRequest;
import com.turkcell.spring_starter.dto.RegisterRequest;

public interface UserService {
    String registerUser(RegisterRequest request);

    String login(LoginRequest request);
}
