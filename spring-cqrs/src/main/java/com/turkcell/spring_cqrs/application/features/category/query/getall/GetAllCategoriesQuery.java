package com.turkcell.spring_cqrs.application.features.category.query.getall;

import org.springframework.data.domain.Page;

import com.turkcell.spring_cqrs.core.mediator.cqrs.Query;

public record GetAllCategoriesQuery(int pageNumber, int pageSize) implements Query<Page<GetAllCategoriesResponse>> {}