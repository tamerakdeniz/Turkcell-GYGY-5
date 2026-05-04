package com.turkcell.spring_cqrs.application.features.category.command.create;

// Command-Query -> DTO

import com.turkcell.spring_cqrs.core.mediator.cqrs.Command;

//Request Dto
public record CreateCategoryCommand(String name) implements Command<CreatedCategoryResponse> {}