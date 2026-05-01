package com.turkcell.spring_starter.service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.turkcell.spring_starter.dto.CreateProductRequest;
import com.turkcell.spring_starter.dto.CreatedProductResponse;
import com.turkcell.spring_starter.dto.GetProductResponse;
import com.turkcell.spring_starter.dto.ListProductResponse;
import com.turkcell.spring_starter.dto.UpdateProductRequest;
import com.turkcell.spring_starter.dto.UpdatedProductResponse;
import com.turkcell.spring_starter.entity.Category;
import com.turkcell.spring_starter.entity.Product;
import com.turkcell.spring_starter.exception.EntityNotFoundException;
import com.turkcell.spring_starter.repository.ProductRepository;

@Service
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final CategoryService categoryService;

    public ProductServiceImpl(ProductRepository productRepository, CategoryService categoryService) {
        this.productRepository = productRepository;
        this.categoryService = categoryService;
    }

    @Override
    public CreatedProductResponse create(CreateProductRequest request) {
        Category category = categoryService.getCategoryById(request.categoryId());

        Product product = new Product();
        product.setName(request.name());
        product.setDescription(request.description());
        product.setCategory(category);

        product = productRepository.save(product);

        return new CreatedProductResponse(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getCategory().getId());
    }

    @Override
    public List<ListProductResponse> getAll() {
        return productRepository.findAll().stream()
                .map(product -> new ListProductResponse(
                        product.getId(),
                        product.getName(),
                        product.getDescription(),
                        product.getCategory().getName()))
                .collect(Collectors.toList());
    }

    @Override
    public GetProductResponse getById(UUID id) {
        Product product = findProductById(id);
        return new GetProductResponse(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getCategory().getId(),
                product.getCategory().getName());
    }

    @Override
    public UpdatedProductResponse update(UUID id, UpdateProductRequest request) {
        Product product = findProductById(id);
        Category category = categoryService.getCategoryById(request.categoryId());

        product.setName(request.name());
        product.setDescription(request.description());
        product.setCategory(category);

        product = productRepository.save(product);

        return new UpdatedProductResponse(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getCategory().getId());
    }

    @Override
    public void delete(UUID id) {
        Product product = findProductById(id);
        productRepository.delete(product);
    }

    private Product findProductById(UUID id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Ürün bulunamadı: " + id));
    }
}
