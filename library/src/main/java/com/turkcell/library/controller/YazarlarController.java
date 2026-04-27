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

import com.turkcell.library.dto.yazar.CreateYazarRequest;
import com.turkcell.library.dto.yazar.UpdateYazarRequest;
import com.turkcell.library.dto.yazar.YazarResponse;
import com.turkcell.library.service.YazarServiceImpl;

@RestController
@RequestMapping("/api/yazarlar")
public class YazarlarController {

    private final YazarServiceImpl yazarService;

    public YazarlarController(YazarServiceImpl yazarService) {
        this.yazarService = yazarService;
    }

    @PostMapping
    public YazarResponse create(@RequestBody CreateYazarRequest request) {
        return yazarService.create(request);
    }

    @GetMapping
    public List<YazarResponse> getAll() {
        return yazarService.getAll();
    }

    @GetMapping("/{id}")
    public YazarResponse getById(@PathVariable Long id) {
        return yazarService.getById(id);
    }

    @PutMapping("/{id}")
    public YazarResponse update(@PathVariable Long id, @RequestBody UpdateYazarRequest request) {
        return yazarService.update(id, request);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        yazarService.delete(id);
    }
}
