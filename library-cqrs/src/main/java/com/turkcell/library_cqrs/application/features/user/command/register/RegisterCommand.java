package com.turkcell.library_cqrs.application.features.user.command.register;

import org.hibernate.validator.constraints.Length;

import com.turkcell.library_cqrs.core.logging.NotLoggableRequest;
import com.turkcell.library_cqrs.core.mediator.cqrs.Command;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

// NotLoggableRequest -> plain password hash'lenmeden önce LoggingBehavior'a uğrar; konsola düşmesin.
public record RegisterCommand(
    @NotBlank @Email String email,
    @NotBlank @Length(min=3) String password
) implements Command<RegisterResponse>, NotLoggableRequest {}
