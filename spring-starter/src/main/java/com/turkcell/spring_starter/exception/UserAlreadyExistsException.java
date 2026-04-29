package com.turkcell.spring_starter.exception;

import org.springframework.http.HttpStatus;

public class UserAlreadyExistsException extends BusinessException {

    public UserAlreadyExistsException(String message) {
        super("Kullanıcı zaten mevcut", "USER_ALREADY_EXISTS", message, HttpStatus.CONFLICT);
    }
}
