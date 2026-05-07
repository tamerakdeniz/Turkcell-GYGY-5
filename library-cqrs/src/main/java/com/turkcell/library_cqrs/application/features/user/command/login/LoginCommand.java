package com.turkcell.library_cqrs.application.features.user.command.login;

import com.turkcell.library_cqrs.core.logging.NotLoggableRequest;
import com.turkcell.library_cqrs.core.mediator.cqrs.Command;

public record LoginCommand(String email, String password) implements Command<LoginResponse>, NotLoggableRequest {}
