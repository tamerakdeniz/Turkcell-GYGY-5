package com.turkcell.spring_cqrs.application.features.category.command.create;

import org.springframework.stereotype.Component;

import com.turkcell.spring_cqrs.application.features.category.rule.CategoryBusinessRules;
import com.turkcell.spring_cqrs.core.mediator.cqrs.CommandHandler;
import com.turkcell.spring_cqrs.domain.Category;
import com.turkcell.spring_cqrs.persistence.repository.CategoryRepository;

@Component
public class CreateCategoryCommandHandler implements CommandHandler<CreateCategoryCommand, CreatedCategoryResponse> 
{
    private final CategoryRepository categoryRepository;
    private final CategoryBusinessRules categoryBusinessRules;
    public CreateCategoryCommandHandler(CategoryRepository categoryRepository, CategoryBusinessRules categoryBusinessRules) {
        this.categoryRepository = categoryRepository;
        this.categoryBusinessRules = categoryBusinessRules;
    }

    @Override
    public CreatedCategoryResponse handle(CreateCategoryCommand command) 
    {
        categoryBusinessRules.categoryWithSameNameMustNotExist(command.name());

        Category category = new Category();
        category.setName(command.name());

        categoryRepository.save(category);


        CreatedCategoryResponse response = new CreatedCategoryResponse(category.getId(), category.getName());
        return response;
    }

}