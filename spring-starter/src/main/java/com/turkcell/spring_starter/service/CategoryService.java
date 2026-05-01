package com.turkcell.spring_starter.service;

import java.util.List;
import java.util.UUID;

import com.turkcell.spring_starter.dto.CreateCategoryRequest;
import com.turkcell.spring_starter.dto.CreatedCategoryResponse;
import com.turkcell.spring_starter.dto.GetCategoryResponse;
import com.turkcell.spring_starter.dto.ListCategoryResponse;
import com.turkcell.spring_starter.dto.UpdateCategoryRequest;
import com.turkcell.spring_starter.dto.UpdatedCategoryResponse;
import com.turkcell.spring_starter.entity.Category;

public interface CategoryService {
    CreatedCategoryResponse create(CreateCategoryRequest request);

    List<ListCategoryResponse> getAll();

    List<ListCategoryResponse> search(String query);

    GetCategoryResponse getById(UUID id);

    UpdatedCategoryResponse update(UUID id, UpdateCategoryRequest request);

    void delete(UUID id);

    // Service-to-service çağrılar için: Category entity'sini doğrulayıp döner.
    Category getCategoryById(UUID id);
}
