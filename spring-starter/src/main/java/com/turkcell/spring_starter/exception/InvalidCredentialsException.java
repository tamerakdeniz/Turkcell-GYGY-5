package com.turkcell.spring_starter.exception;

import org.springframework.http.HttpStatus;

public class InvalidCredentialsException extends BusinessException {

    public InvalidCredentialsException(String message) {
        super("Geçersiz kimlik bilgileri", "INVALID_CREDENTIALS", message, HttpStatus.UNAUTHORIZED);
    }
}
