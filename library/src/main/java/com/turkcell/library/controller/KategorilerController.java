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

import com.turkcell.library.dto.kategori.CreateKategoriRequest;
import com.turkcell.library.dto.kategori.KategoriResponse;
import com.turkcell.library.dto.kategori.UpdateKategoriRequest;
import com.turkcell.library.service.KategoriServiceImpl;

@RestController
@RequestMapping("/api/kategoriler")
public class KategorilerController {

    private final KategoriServiceImpl kategoriService;

    public KategorilerController(KategoriServiceImpl kategoriService) {
        this.kategoriService = kategoriService;
    }

    @PostMapping
    public KategoriResponse create(@RequestBody CreateKategoriRequest request) {
        return kategoriService.create(request);
    }

    @GetMapping
    public List<KategoriResponse> getAll() {
        return kategoriService.getAll();
    }

    @GetMapping("/{id}")
    public KategoriResponse getById(@PathVariable Long id) {
        return kategoriService.getById(id);
    }

    @PutMapping("/{id}")
    public KategoriResponse update(@PathVariable Long id, @RequestBody UpdateKategoriRequest request) {
        return kategoriService.update(id, request);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        kategoriService.delete(id);
    }
}
