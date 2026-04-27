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

import com.turkcell.library.dto.oduncalma.CreateOduncAlmaRequest;
import com.turkcell.library.dto.oduncalma.OduncAlmaResponse;
import com.turkcell.library.dto.oduncalma.UpdateOduncAlmaRequest;
import com.turkcell.library.service.OduncAlmaServiceImpl;

@RestController
@RequestMapping("/api/odunc-almalar")
public class OduncAlmalarController {

    private final OduncAlmaServiceImpl oduncAlmaService;

    public OduncAlmalarController(OduncAlmaServiceImpl oduncAlmaService) {
        this.oduncAlmaService = oduncAlmaService;
    }

    @PostMapping
    public OduncAlmaResponse create(@RequestBody CreateOduncAlmaRequest request) {
        return oduncAlmaService.create(request);
    }

    @GetMapping
    public List<OduncAlmaResponse> getAll() {
        return oduncAlmaService.getAll();
    }

    @GetMapping("/{id}")
    public OduncAlmaResponse getById(@PathVariable Long id) {
        return oduncAlmaService.getById(id);
    }

    @PutMapping("/{id}")
    public OduncAlmaResponse update(@PathVariable Long id, @RequestBody UpdateOduncAlmaRequest request) {
        return oduncAlmaService.update(id, request);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        oduncAlmaService.delete(id);
    }
}
