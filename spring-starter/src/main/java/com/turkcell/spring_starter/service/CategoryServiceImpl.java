package com.turkcell.spring_starter.service;

import java.util.List;
import java.util.Set;
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
import com.turkcell.spring_starter.repository.CategoryRepository;

import jakarta.persistence.EntityManager;

@Service
public class CategoryServiceImpl {
    private final CategoryRepository categoryRepository;
    private final EntityManager entityManager;

        public CategoryServiceImpl(CategoryRepository categoryRepository, EntityManager entityManager) {
            this.categoryRepository = categoryRepository;
            this.entityManager = entityManager;
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

            List<ListCategoryResponse> response = categories.stream().map(category -> {
            ListCategoryResponse listCategoryResponse = new ListCategoryResponse();
            listCategoryResponse.setId(category.getId());
            listCategoryResponse.setName(category.getName());
            return listCategoryResponse;
        }).collect(Collectors.toList());

        return response;
    }


    public List<ListCategoryResponse> search(String query) {        

        // Set<Category> categories = categoryRepository.findByNameLike("%" + query + "%");

        // String Concatination -> KESİNLİKLE YASAK
        // String jpql = "Select c from Category c Where c.name LIKE '%" + query + "%'";

        String jpql = "Select c from Category c Where c.name like :query";

        List<Category> categories = entityManager
        .createQuery(jpql, Category.class)
        .setParameter("query", "%" + query + "%")
        .getResultList();

        List<ListCategoryResponse> responseList = categories.stream().map(category -> {
            ListCategoryResponse listCategoryResponse = new ListCategoryResponse();
            listCategoryResponse.setId(category.getId());
            listCategoryResponse.setName(category.getName());
            return listCategoryResponse;
        }).collect(Collectors.toList());

        return responseList;
    }

    public GetCategoryResponse getById(UUID id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found with id: " + id));

        GetCategoryResponse response = new GetCategoryResponse();
        response.setId(category.getId());
        response.setName(category.getName());

        return response;
    }

    public UpdatedCategoryResponse update(UUID id, UpdateCategoryRequest updateCategoryRequest) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found with id: " + id));

        category.setName(updateCategoryRequest.getName());
        category = categoryRepository.save(category);

        UpdatedCategoryResponse response = new UpdatedCategoryResponse();
        response.setId(category.getId());
        response.setName(category.getName());

        return response;
    }

    public void delete(UUID id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found with id: " + id));

        categoryRepository.delete(category);
    }
}
