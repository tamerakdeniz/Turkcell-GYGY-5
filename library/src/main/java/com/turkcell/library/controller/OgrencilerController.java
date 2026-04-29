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

import com.turkcell.library.dto.ogrenci.CreateOgrenciRequest;
import com.turkcell.library.dto.ogrenci.OgrenciResponse;
import com.turkcell.library.dto.ogrenci.UpdateOgrenciRequest;
import com.turkcell.library.service.OgrenciServiceImpl;

@RestController
@RequestMapping("/api/ogrenciler")
public class OgrencilerController {

    private final OgrenciServiceImpl ogrenciService;

    public OgrencilerController(OgrenciServiceImpl ogrenciService) {
        this.ogrenciService = ogrenciService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public OgrenciResponse create(@RequestBody CreateOgrenciRequest request) {
        return ogrenciService.create(request);
    }

    @GetMapping
    public List<OgrenciResponse> getAll() {
        return ogrenciService.getAll();
    }

    @GetMapping("/{id}")
    public OgrenciResponse getById(@PathVariable Long id) {
        return ogrenciService.getById(id);
    }

    @PutMapping("/{id}")
    public OgrenciResponse update(@PathVariable Long id, @RequestBody UpdateOgrenciRequest request) {
        return ogrenciService.update(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        ogrenciService.delete(id);
    }
}
