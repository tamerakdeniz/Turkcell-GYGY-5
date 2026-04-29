package com.turkcell.library.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.turkcell.library.dto.kitap.CreateKitapRequest;
import com.turkcell.library.dto.kitap.KitapResponse;
import com.turkcell.library.dto.kitap.UpdateKitapRequest;
import com.turkcell.library.service.KitapServiceImpl;

@RestController
@RequestMapping("/api/kitaplar")
public class KitaplarController {

    private final KitapServiceImpl kitapService;

    public KitaplarController(KitapServiceImpl kitapService) {
        this.kitapService = kitapService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public KitapResponse create(@RequestBody CreateKitapRequest request) {
        return kitapService.create(request);
    }

    @GetMapping
    public List<KitapResponse> getAll() {
        return kitapService.getAll();
    }

    @GetMapping("/{id}")
    public KitapResponse getById(@PathVariable Long id) {
        return kitapService.getById(id);
    }

    @PutMapping("/{id}")
    public KitapResponse update(@PathVariable Long id, @RequestBody UpdateKitapRequest request) {
        return kitapService.update(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        kitapService.delete(id);
    }
}
