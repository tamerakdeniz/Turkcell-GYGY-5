package com.turkcell.library_cqrs.application.features.category.command.create;

import org.hibernate.validator.constraints.Length;

import com.turkcell.library_cqrs.core.mediator.cqrs.Command;

import jakarta.validation.constraints.NotBlank;

// Command-Query -> DTO
public record CreateCategoryCommand(
    @NotBlank @Length(min = 3, max = 100) String name,
    @Length(max = 500) String description
) implements Command<CreatedCategoryResponse> {}
