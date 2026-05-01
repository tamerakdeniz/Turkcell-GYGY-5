package com.turkcell.spring_starter.service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.turkcell.spring_starter.dto.CreateCategoryRequest;
import com.turkcell.spring_starter.dto.CreatedCategoryResponse;
import com.turkcell.spring_starter.dto.GetCategoryResponse;
import com.turkcell.spring_starter.dto.ListCategoryResponse;
import com.turkcell.spring_starter.dto.UpdateCategoryRequest;
import com.turkcell.spring_starter.dto.UpdatedCategoryResponse;
import com.turkcell.spring_starter.entity.Category;
import com.turkcell.spring_starter.exception.EntityNotFoundException;
import com.turkcell.spring_starter.repository.CategoryRepository;

import jakarta.persistence.EntityManager;

@Service
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final EntityManager entityManager;

    public CategoryServiceImpl(CategoryRepository categoryRepository, EntityManager entityManager) {
        this.categoryRepository = categoryRepository;
        this.entityManager = entityManager;
    }

    @Override
    public CreatedCategoryResponse create(CreateCategoryRequest request) {
        Category category = new Category();
        category.setName(request.name());

        category = categoryRepository.save(category);

        return new CreatedCategoryResponse(category.getId(), category.getName());
    }

    @Override
    public List<ListCategoryResponse> getAll() {
        return categoryRepository.findAll().stream()
                .map(category -> new ListCategoryResponse(category.getId(), category.getName()))
                .collect(Collectors.toList());
    }

    @Override
    public List<ListCategoryResponse> search(String query) {
        // String concatenation yerine parametreli JPQL — SQL injection güvenli.
        String jpql = "Select c from Category c Where c.name like :query";

        List<Category> categories = entityManager
                .createQuery(jpql, Category.class)
                .setParameter("query", "%" + query + "%")
                .getResultList();

        return categories.stream()
                .map(category -> new ListCategoryResponse(category.getId(), category.getName()))
                .collect(Collectors.toList());
    }

    @Override
    public GetCategoryResponse getById(UUID id) {
        Category category = getCategoryById(id);
        return new GetCategoryResponse(category.getId(), category.getName());
    }

    @Override
    public UpdatedCategoryResponse update(UUID id, UpdateCategoryRequest request) {
        Category category = getCategoryById(id);
        category.setName(request.name());
        category = categoryRepository.save(category);

        return new UpdatedCategoryResponse(category.getId(), category.getName());
    }

    @Override
    public void delete(UUID id) {
        Category category = getCategoryById(id);
        categoryRepository.delete(category);
    }

    @Override
    public Category getCategoryById(UUID id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Kategori bulunamadı: " + id));
    }
}
