package com.turkcell.spring_starter.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.turkcell.spring_starter.dto.CreateCategoryRequest;
import com.turkcell.spring_starter.dto.CreatedCategoryResponse;
import com.turkcell.spring_starter.dto.ListCategoryResponse;
import com.turkcell.spring_starter.entity.Category;
import com.turkcell.spring_starter.repository.CategoryRepository;

@Service
public class CategoryServiceImpl {
    private final CategoryRepository categoryRepository;

    public CategoryServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public CreatedCategoryResponse create(CreateCategoryRequest createCategoryRequest) {
        // Veritabanında insert-update işlemi çalıştırır. Eğer id alanı null ise insert, değilse update işlemi yapar.
        // Entity id'e sahipse update işlemi yapar, yoksa (null) insert işlemi yapar.

        Category category = new Category();
        category.setName(createCategoryRequest.getName());

        category = this.categoryRepository.save(category); // ekledikten sonraki halini al

        CreatedCategoryResponse response = new CreatedCategoryResponse();
        response.setId(category.getId());
        response.setName(category.getName());

        return response;
    } 

    public List<ListCategoryResponse> getAll() {
        List<Category> categories = categoryRepository.findAll();

        // TODO: Refactor
            List<ListCategoryResponse> response = categories.stream().map(category -> {
            ListCategoryResponse listCategoryResponse = new ListCategoryResponse();
            listCategoryResponse.setId(category.getId());
            listCategoryResponse.setName(category.getName());
            return listCategoryResponse;
        }).collect(Collectors.toList());

        return response;
    }
}

