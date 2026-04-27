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

import com.turkcell.library.dto.ceza.CezaResponse;
import com.turkcell.library.dto.ceza.CreateCezaRequest;
import com.turkcell.library.dto.ceza.UpdateCezaRequest;
import com.turkcell.library.service.CezaServiceImpl;

@RestController
@RequestMapping("/api/cezalar")
public class CezalarController {

    private final CezaServiceImpl cezaService;

    public CezalarController(CezaServiceImpl cezaService) {
        this.cezaService = cezaService;
    }

    @PostMapping
    public CezaResponse create(@RequestBody CreateCezaRequest request) {
        return cezaService.create(request);
    }

    @GetMapping
    public List<CezaResponse> getAll() {
        return cezaService.getAll();
    }

    @GetMapping("/{id}")
    public CezaResponse getById(@PathVariable Long id) {
        return cezaService.getById(id);
    }

    @PutMapping("/{id}")
    public CezaResponse update(@PathVariable Long id, @RequestBody UpdateCezaRequest request) {
        return cezaService.update(id, request);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        cezaService.delete(id);
    }
}
