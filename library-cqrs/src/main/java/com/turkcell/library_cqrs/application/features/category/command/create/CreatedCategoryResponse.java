package com.turkcell.library_cqrs.application.features.category.command.create;

import java.util.UUID;

public record CreatedCategoryResponse(UUID id, String name, String description) {}
