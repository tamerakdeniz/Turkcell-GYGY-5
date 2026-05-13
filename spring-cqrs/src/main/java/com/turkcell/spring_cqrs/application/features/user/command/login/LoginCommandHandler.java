package com.turkcell.spring_cqrs.application.features.user.command.login;

import java.util.List;

import org.springframework.stereotype.Component;

import com.turkcell.spring_cqrs.application.features.user.rule.UserBusinessRules;
import com.turkcell.spring_cqrs.core.mediator.cqrs.CommandHandler;
import com.turkcell.spring_cqrs.core.security.jwt.JwtService;
import com.turkcell.spring_cqrs.domain.User;

@Component
public class LoginCommandHandler implements CommandHandler<LoginCommand, LoginResponse> {
    private final JwtService jwtService;
    private final UserBusinessRules userBusinessRules;

    public LoginCommandHandler(JwtService jwtService, UserBusinessRules userBusinessRules) {
        this.jwtService = jwtService;
        this.userBusinessRules = userBusinessRules;
    }


    @Override
    public LoginResponse handle(LoginCommand command) {
        User user = userBusinessRules.userWithEmailMustExist(command.email());
        userBusinessRules.userPasswordMustMatch(command.password(), user.getPassword());

        String jwt = jwtService.generate(user.getId(), user.getEmail(), List.of(user.getRole()));
        return new LoginResponse(jwt);
    }
}
