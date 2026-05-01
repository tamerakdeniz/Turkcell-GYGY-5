package com.turkcell.spring_cqrs.application.features.category.command.create;

import java.util.UUID;

import com.turkcell.spring_cqrs.core.mediator.cqrs.Command;

// Command-Query -> DTO
public record CreateCategoryCommand(String name,String description) implements Command<UUID> {}
