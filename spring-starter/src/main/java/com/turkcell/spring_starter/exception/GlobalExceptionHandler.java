package com.turkcell.spring_starter.exception;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.turkcell.spring_starter.dto.ErrorResponse;
import com.turkcell.spring_starter.dto.ValidationErrorResponse;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorResponse> handleBusinessException(BusinessException exception) {
        ErrorResponse response = new ErrorResponse(
                exception.getTitle(),
                exception.getType(),
                exception.getMessage());
        return ResponseEntity.status(exception.getStatus()).body(response);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorResponse> handleRuntimeException(RuntimeException exception) {
        ErrorResponse response = new ErrorResponse(
                "Beklenmeyen hata",
                "INTERNAL_ERROR",
                exception.getMessage());
        return ResponseEntity.status(org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<List<ValidationErrorResponse>> handleMethodArgumentNotValidException(
            MethodArgumentNotValidException exception) {

        Map<String, List<String>> errorsByField = new HashMap<>();
        for (FieldError fieldError : exception.getBindingResult().getFieldErrors()) {
            errorsByField
                    .computeIfAbsent(fieldError.getField(), key -> new ArrayList<>())
                    .add(fieldError.getDefaultMessage());
        }

        List<ValidationErrorResponse> body = new ArrayList<>();
        for (Map.Entry<String, List<String>> entry : errorsByField.entrySet()) {
            body.add(new ValidationErrorResponse(entry.getKey(), entry.getValue()));
        }

        return ResponseEntity.status(org.springframework.http.HttpStatus.BAD_REQUEST).body(body);
    }
}
