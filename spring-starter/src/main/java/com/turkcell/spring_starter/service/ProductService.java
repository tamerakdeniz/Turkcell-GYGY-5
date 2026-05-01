package com.turkcell.spring_starter.service;

import java.util.List;
import java.util.UUID;

import com.turkcell.spring_starter.dto.CreateProductRequest;
import com.turkcell.spring_starter.dto.CreatedProductResponse;
import com.turkcell.spring_starter.dto.GetProductResponse;
import com.turkcell.spring_starter.dto.ListProductResponse;
import com.turkcell.spring_starter.dto.UpdateProductRequest;
import com.turkcell.spring_starter.dto.UpdatedProductResponse;

public interface ProductService {
    CreatedProductResponse create(CreateProductRequest request);

    List<ListProductResponse> getAll();

    GetProductResponse getById(UUID id);

    UpdatedProductResponse update(UUID id, UpdateProductRequest request);

    void delete(UUID id);
}
