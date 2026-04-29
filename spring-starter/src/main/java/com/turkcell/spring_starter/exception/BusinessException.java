package com.turkcell.spring_starter.exception;

import org.springframework.http.HttpStatus;

public abstract class BusinessException extends RuntimeException {

    private final String title;
    private final String type;
    private final HttpStatus status;

    protected BusinessException(String title, String type, String message, HttpStatus status) {
        super(message);
        this.title = title;
        this.type = type;
        this.status = status;
    }

    public String getTitle() {
        return title;
    }

    public String getType() {
        return type;
    }

    public HttpStatus getStatus() {
        return status;
    }
}
