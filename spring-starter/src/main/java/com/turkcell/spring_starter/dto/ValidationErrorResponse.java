package com.turkcell.spring_starter.dto;

import java.util.List;

public class ValidationErrorResponse {
    private String argument;
    private List<String> message;

    public ValidationErrorResponse() {
    }

    public ValidationErrorResponse(String argument, List<String> message) {
        this.argument = argument;
        this.message = message;
    }

    public String getArgument() {
        return argument;
    }

    public void setArgument(String argument) {
        this.argument = argument;
    }

    public List<String> getMessage() {
        return message;
    }

    public void setMessage(List<String> message) {
        this.message = message;
    }
}
