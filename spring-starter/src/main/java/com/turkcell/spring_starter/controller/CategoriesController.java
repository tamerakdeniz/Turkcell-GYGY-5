package com.turkcell.spring_starter.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.turkcell.spring_starter.dto.CreateCategoryRequest;
import com.turkcell.spring_starter.dto.CreatedCategoryResponse;
import com.turkcell.spring_starter.dto.GetCategoryResponse;
import com.turkcell.spring_starter.dto.ListCategoryResponse;
import com.turkcell.spring_starter.dto.UpdateCategoryRequest;
import com.turkcell.spring_starter.dto.UpdatedCategoryResponse;
import com.turkcell.spring_starter.service.CategoryService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/categories")
public class CategoriesController {
    private final CategoryService categoryService;

    public CategoriesController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @PostMapping
    public CreatedCategoryResponse create(@RequestBody @Valid CreateCategoryRequest request) {
        return categoryService.create(request);
    }

    @GetMapping
    public List<ListCategoryResponse> getAll() {
        return categoryService.getAll();
    }

    @GetMapping("/search")
    public List<ListCategoryResponse> search(@RequestParam String query) {
        return categoryService.search(query);
    }

    @GetMapping("/{id}")
    public GetCategoryResponse getById(@PathVariable UUID id) {
        return categoryService.getById(id);
    }

    @PutMapping("/{id}")
    public UpdatedCategoryResponse update(@PathVariable UUID id,
                                          @RequestBody @Valid UpdateCategoryRequest request) {
        return categoryService.update(id, request);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable UUID id) {
        categoryService.delete(id);
    }
}
