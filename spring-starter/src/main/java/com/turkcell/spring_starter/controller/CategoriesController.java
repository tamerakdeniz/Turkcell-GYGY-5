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
import org.springframework.web.bind.annotation.RestController;

import com.turkcell.spring_starter.dto.CreateCategoryRequest;
import com.turkcell.spring_starter.dto.CreatedCategoryResponse;
import com.turkcell.spring_starter.dto.GetCategoryResponse;
import com.turkcell.spring_starter.dto.ListCategoryResponse;
import com.turkcell.spring_starter.dto.UpdateCategoryRequest;
import com.turkcell.spring_starter.dto.UpdatedCategoryResponse;
import com.turkcell.spring_starter.service.CategoryServiceImpl;

// GBu projedeki tüm entityler için tüm CRUD işlemleri kodlanmalı.
// GET-GET BY ID-ADD-UPDATE-DELETE işlemleri kodlanmalı. (CRUD)
// Kütüphane sisteminizi code-first oluşturun.

// (sonraki ders) JPQL (Java Persistence Query Language) : SQL'e benzer bir sorgu dilidir. Entityler üzerinden sorgulama yapmamızı sağlar. SQL'den farklı olarak tablo isimleri yerine entity isimleri kullanılır. SQL'de kullanılan join, where, group by gibi ifadeler JPQL'de de kullanılabilir. JPQL, JPA tarafından sağlanan bir sorgu dilidir ve JPA'nın EntityManager aracılığıyla çalışır. JPQL sorguları, veritabanı bağımsızdır ve farklı veritabanlarında çalışabilir. JPQL, SQL'e benzer bir sözdizimine sahip olduğu için öğrenmesi kolaydır ve JPA ile birlikte kullanıldığında güçlü bir sorgulama yeteneği sağlar.


@RestController
@RequestMapping("/api/categories")
public class CategoriesController {
    private final CategoryServiceImpl categoryServiceImpl;

    public CategoriesController(CategoryServiceImpl categoryServiceImpl) {
        this.categoryServiceImpl = categoryServiceImpl;
    }

    @PostMapping()
    public CreatedCategoryResponse create(@RequestBody CreateCategoryRequest createCategoryRequest)
    {
       return categoryServiceImpl.create(createCategoryRequest);
    }

    @GetMapping
    public List<ListCategoryResponse> getAll() {
        return categoryServiceImpl.getAll();
    }

    @GetMapping("/{id}")
    public GetCategoryResponse getById(@PathVariable UUID id) {
        return categoryServiceImpl.getById(id);
    }

    @PutMapping("/{id}")
    public UpdatedCategoryResponse update(@PathVariable UUID id, @RequestBody UpdateCategoryRequest updateCategoryRequest) {
        return categoryServiceImpl.update(id, updateCategoryRequest);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable UUID id) {
        categoryServiceImpl.delete(id);
    }
}
