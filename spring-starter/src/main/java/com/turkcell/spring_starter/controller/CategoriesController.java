package com.turkcell.spring_starter.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.turkcell.spring_starter.entity.Category;
import com.turkcell.spring_starter.service.CategoryServiceImp;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/api/categories")
public class CategoriesController {
    private final CategoryServiceImp categoryService;

    public CategoriesController(CategoryServiceImp categoryService) {
        this.categoryService = categoryService;
    }

    @PostMapping
    public void create(@RequestBody Category category) {
        categoryService.create(category);
    }
}
