package com.turkcell.spring_starter.dto;

import org.hibernate.validator.constraints.Length;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record RegisterRequest(
    @NotBlank
    @Email
    String email,
    @NotBlank(message = "Parola boş olamaz.")
    @Length(min = 6, max = 100, message = "Parola en az 6, en fazla 100 karakter olabilir.")
    String password
) {}
