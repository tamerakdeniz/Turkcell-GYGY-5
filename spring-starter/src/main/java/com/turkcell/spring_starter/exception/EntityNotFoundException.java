package com.turkcell.spring_starter.exception;

import org.springframework.http.HttpStatus;

public class EntityNotFoundException extends BusinessException {

    public EntityNotFoundException(String message) {
        super("Kayıt bulunamadı", "ENTITY_NOT_FOUND", message, HttpStatus.NOT_FOUND);
    }
}
