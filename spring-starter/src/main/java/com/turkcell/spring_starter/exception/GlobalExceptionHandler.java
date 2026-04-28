package com.turkcell.spring_starter.exception;



import java.lang.reflect.Method;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;


// Ödev: Bilindik hata türleri için yönetimi düzgünleştir.
// RuntimeException çok genel olduğu için, kendimize özel Exception türleri yaratıp onları yakalayarak daha spesifik mesajlar dönebiliriz. 
// (BusinessException gibi bir üst sınıf yaratıp, onun altına UserAlreadyExistsException, InvalidCredentialsException gibi özel exception'lar yaratabiliriz.) 
// Mesela, UserAlreadyExistsException, InvalidCredentialsException gibi.
// ErrorResponse -> {title, type, message}
// ValidationErrorResponse -> {argument, [message]}
@RestControllerAdvice
public class GlobalExceptionHandler {
   @ExceptionHandler({RuntimeException.class})
   @ResponseStatus(HttpStatus.BAD_REQUEST)
   public String handleRuntimeException(RuntimeException exception) {
        return exception.getMessage();
   }

   @ExceptionHandler({MethodArgumentNotValidException.class})
   @ResponseStatus(HttpStatus.BAD_REQUEST)
   public String handleMethodArgumentNotValidException(MethodArgumentNotValidException exception) {
        return exception.getMessage();
   }

}