package com.turkcell.library_cqrs.application.features.category.mapper;

import org.springframework.stereotype.Component;

import com.turkcell.library_cqrs.application.features.category.command.create.CreateCategoryCommand;
import com.turkcell.library_cqrs.application.features.category.command.create.CreatedCategoryResponse;
import com.turkcell.library_cqrs.application.features.category.query.getall.GetAllCategoriesResponse;
import com.turkcell.library_cqrs.domain.Category;

@Component
public class CategoryMapper {
    public Category categoryFromCreateCommand(CreateCategoryCommand command)
    {
        Category category = new Category();
        category.setName(command.name());
        category.setDescription(command.description());
        return category;
    }

    public CreatedCategoryResponse createdCategoryResponseFromCategory(Category category)
    {
        return new CreatedCategoryResponse(category.getId(), category.getName(), category.getDescription());
    }

    public GetAllCategoriesResponse getAllCategoriesResponseFromCategory(Category category)
    {
        return new GetAllCategoriesResponse(category.getId(), category.getName(), category.getDescription());
    }
}
