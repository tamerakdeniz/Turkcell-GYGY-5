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

import com.turkcell.spring_starter.dto.CreateTagRequest;
import com.turkcell.spring_starter.dto.CreatedTagResponse;
import com.turkcell.spring_starter.dto.GetTagResponse;
import com.turkcell.spring_starter.dto.ListTagResponse;
import com.turkcell.spring_starter.dto.UpdateTagRequest;
import com.turkcell.spring_starter.dto.UpdatedTagResponse;
import com.turkcell.spring_starter.service.TagService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/tags")
public class TagsController {
    private final TagService tagService;

    public TagsController(TagService tagService) {
        this.tagService = tagService;
    }

    @PostMapping
    public CreatedTagResponse create(@RequestBody @Valid CreateTagRequest request) {
        return tagService.create(request);
    }

    @GetMapping
    public List<ListTagResponse> getAll() {
        return tagService.getAll();
    }

    @GetMapping("/{id}")
    public GetTagResponse getById(@PathVariable UUID id) {
        return tagService.getById(id);
    }

    @PutMapping("/{id}")
    public UpdatedTagResponse update(@PathVariable UUID id,
                                     @RequestBody @Valid UpdateTagRequest request) {
        return tagService.update(id, request);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable UUID id) {
        tagService.delete(id);
    }
}
