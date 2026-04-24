package com.turkcell.spring_starter.service;

import org.springframework.stereotype.Service;

import com.turkcell.spring_starter.entity.Category;
import com.turkcell.spring_starter.repository.CategoryRepository;

@Service
public class CategoryServiceImp {
    private final CategoryRepository categoryRepository;

    public CategoryServiceImp(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public void create(Category category) {
        // Veritabanında insert-update işlemi çalıştırır. Eğer id alanı null ise insert, değilse update işlemi yapar.
        // Entity id'e sahipse update işlemi yapar, yoksa (null) insert işlemi yapar.
        categoryRepository.save(category);
    }

}
