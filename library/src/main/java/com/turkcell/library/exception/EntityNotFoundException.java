package com.turkcell.library.exception;

import org.springframework.http.HttpStatus;

public class EntityNotFoundException extends BusinessException {

    public EntityNotFoundException(String entityName, Object id) {
        super(
                entityName + " bulunamadı",
                entityName.toUpperCase() + "_NOT_FOUND",
                entityName + " bulunamadı: " + id,
                HttpStatus.NOT_FOUND);
    }
}
