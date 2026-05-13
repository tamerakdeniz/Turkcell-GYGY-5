package com.turkcell.spring_cqrs.core.security.authorization;

public class AuthenticationException extends RuntimeException {
    public AuthenticationException(String message) {
        super(message);
    }
}
