package com.turkcell.spring_cqrs.application.features.category.command.create;

import java.util.UUID;

import org.springframework.stereotype.Component;

import com.turkcell.spring_cqrs.core.mediator.cqrs.CommandHandler;

@Component
public class CreateCategoryCommandHandler implements CommandHandler<CreateCategoryCommand, UUID> {

    @Override
    public UUID handle(CreateCategoryCommand command) {
        System.out.println("Create command çalıştı");
        return UUID.randomUUID();
    }

}
