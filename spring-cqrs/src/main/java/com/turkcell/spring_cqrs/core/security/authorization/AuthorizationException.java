package com.turkcell.spring_cqrs.core.security.authorization;

public class AuthorizationException extends RuntimeException {
    public AuthorizationException(String message) {
        super(message);
    }
}
