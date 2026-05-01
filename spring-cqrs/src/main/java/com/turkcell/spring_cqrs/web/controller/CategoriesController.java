package com.turkcell.spring_cqrs.web.controller;

import java.util.UUID;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.turkcell.spring_cqrs.application.features.category.command.create.CreateCategoryCommand;
import com.turkcell.spring_cqrs.core.mediator.Mediator;

@RequestMapping("/api/categories")
@RestController
public class CategoriesController {
    private final Mediator mediator;

    public CategoriesController(Mediator mediator) {
        this.mediator = mediator;
    }

    @PostMapping
    public UUID create(@RequestBody CreateCategoryCommand command){
        return mediator.send(command);
    }
}