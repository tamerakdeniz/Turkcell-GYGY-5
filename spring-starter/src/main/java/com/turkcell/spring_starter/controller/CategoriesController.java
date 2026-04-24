package com.turkcell.spring_starter.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.turkcell.spring_starter.service.CategoryServiceImp;

@RestController
@RequestMapping("/api/categories")
public class CategoriesController {
    private final CategoryServiceImp categoryService;

    public CategoriesController(CategoryServiceImp categoryService) {
        this.categoryService = categoryService;
    }
}
