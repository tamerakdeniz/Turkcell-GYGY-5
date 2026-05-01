package com.turkcell.spring_starter.dto;

import java.util.List;

public record ValidationErrorResponse(String argument, List<String> message) {}
