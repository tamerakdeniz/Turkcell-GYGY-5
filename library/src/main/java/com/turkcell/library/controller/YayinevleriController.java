package com.turkcell.library.controller;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.turkcell.library.dto.yayinevi.CreateYayineviRequest;
import com.turkcell.library.dto.yayinevi.UpdateYayineviRequest;
import com.turkcell.library.dto.yayinevi.YayineviResponse;
import com.turkcell.library.service.YayineviServiceImpl;

@RestController
@RequestMapping("/api/yayinevleri")
public class YayinevleriController {

    private final YayineviServiceImpl yayineviService;

    public YayinevleriController(YayineviServiceImpl yayineviService) {
        this.yayineviService = yayineviService;
    }

    @PostMapping
    public YayineviResponse create(@RequestBody CreateYayineviRequest request) {
        return yayineviService.create(request);
    }

    @GetMapping
    public List<YayineviResponse> getAll() {
        return yayineviService.getAll();
    }

    @GetMapping("/{id}")
    public YayineviResponse getById(@PathVariable Long id) {
        return yayineviService.getById(id);
    }

    @PutMapping("/{id}")
    public YayineviResponse update(@PathVariable Long id, @RequestBody UpdateYayineviRequest request) {
        return yayineviService.update(id, request);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        yayineviService.delete(id);
    }
}
