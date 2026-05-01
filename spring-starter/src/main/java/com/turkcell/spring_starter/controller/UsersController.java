package com.turkcell.spring_starter.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.turkcell.spring_starter.dto.LoginRequest;
import com.turkcell.spring_starter.dto.RegisterRequest;
import com.turkcell.spring_starter.service.UserService;

import jakarta.validation.Valid;

@RequestMapping("/api/users")
@RestController
public class UsersController {
    private final UserService userService;

    public UsersController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public String register(@RequestBody @Valid RegisterRequest request) {
        return userService.registerUser(request);
    }

    @PostMapping("login")
    @ResponseStatus(HttpStatus.OK)
    public String login(@RequestBody @Valid LoginRequest request) {
        return userService.login(request);
    }
}
