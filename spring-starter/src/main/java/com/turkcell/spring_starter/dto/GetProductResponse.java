package com.turkcell.spring_starter.dto;

import java.util.UUID;

public record GetProductResponse(
    UUID id,
    String name,
    String description,
    UUID categoryId,
    String categoryName
) {}
