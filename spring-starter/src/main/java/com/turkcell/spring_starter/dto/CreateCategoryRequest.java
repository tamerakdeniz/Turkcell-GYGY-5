package com.turkcell.spring_starter.dto;

import org.hibernate.validator.constraints.Length;

import jakarta.validation.constraints.NotBlank;

public record CreateCategoryRequest(
    @NotBlank
    @Length(min = 2, max = 100)
    String name
) {}
