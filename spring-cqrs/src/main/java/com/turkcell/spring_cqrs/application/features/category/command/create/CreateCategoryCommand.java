package com.turkcell.spring_cqrs.application.features.category.command.create;

// Command-Query -> DTO

import java.util.UUID;

import com.turkcell.spring_cqrs.core.mediator.cqrs.Command;

public record CreateCategoryCommand(String name) implements Command<UUID> {}