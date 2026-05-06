package com.turkcell.library_cqrs.application.features.category.query.getall;

import java.util.UUID;

public record GetAllCategoriesResponse(UUID id, String name, String description) {}
