package com.turkcell.spring_starter.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.turkcell.spring_starter.dto.CreateCategoryRequest;
import com.turkcell.spring_starter.dto.CreatedCategoryResponse;
import com.turkcell.spring_starter.dto.ListCategoryResponse;
import com.turkcell.spring_starter.entity.Category;
import com.turkcell.spring_starter.service.CategoryServiceImpl;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

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
}