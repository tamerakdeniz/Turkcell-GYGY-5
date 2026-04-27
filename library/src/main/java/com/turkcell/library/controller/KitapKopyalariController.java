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

import com.turkcell.library.dto.kitapkopya.CreateKitapKopyaRequest;
import com.turkcell.library.dto.kitapkopya.KitapKopyaResponse;
import com.turkcell.library.dto.kitapkopya.UpdateKitapKopyaRequest;
import com.turkcell.library.service.KitapKopyaServiceImpl;

@RestController
@RequestMapping("/api/kitap-kopyalari")
public class KitapKopyalariController {

    private final KitapKopyaServiceImpl kitapKopyaService;

    public KitapKopyalariController(KitapKopyaServiceImpl kitapKopyaService) {
        this.kitapKopyaService = kitapKopyaService;
    }

    @PostMapping
    public KitapKopyaResponse create(@RequestBody CreateKitapKopyaRequest request) {
        return kitapKopyaService.create(request);
    }

    @GetMapping
    public List<KitapKopyaResponse> getAll() {
        return kitapKopyaService.getAll();
    }

    @GetMapping("/{id}")
    public KitapKopyaResponse getById(@PathVariable Long id) {
        return kitapKopyaService.getById(id);
    }

    @PutMapping("/{id}")
    public KitapKopyaResponse update(@PathVariable Long id, @RequestBody UpdateKitapKopyaRequest request) {
        return kitapKopyaService.update(id, request);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        kitapKopyaService.delete(id);
    }
}
