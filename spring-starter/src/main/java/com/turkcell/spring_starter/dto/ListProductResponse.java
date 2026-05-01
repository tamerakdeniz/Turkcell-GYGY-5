package com.turkcell.spring_starter.dto;

import java.util.UUID;

public record ListProductResponse(
    UUID id,
    String name,
    String description,
    String categoryName
) {}
