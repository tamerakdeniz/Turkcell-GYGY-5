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

import com.turkcell.library.dto.rezervasyon.CreateRezervasyonRequest;
import com.turkcell.library.dto.rezervasyon.RezervasyonResponse;
import com.turkcell.library.dto.rezervasyon.UpdateRezervasyonRequest;
import com.turkcell.library.service.RezervasyonServiceImpl;

@RestController
@RequestMapping("/api/rezervasyonlar")
public class RezervasyonlarController {

    private final RezervasyonServiceImpl rezervasyonService;

    public RezervasyonlarController(RezervasyonServiceImpl rezervasyonService) {
        this.rezervasyonService = rezervasyonService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public RezervasyonResponse create(@RequestBody CreateRezervasyonRequest request) {
        return rezervasyonService.create(request);
    }

    @GetMapping
    public List<RezervasyonResponse> getAll() {
        return rezervasyonService.getAll();
    }

    @GetMapping("/{id}")
    public RezervasyonResponse getById(@PathVariable Long id) {
        return rezervasyonService.getById(id);
    }

    @PutMapping("/{id}")
    public RezervasyonResponse update(@PathVariable Long id, @RequestBody UpdateRezervasyonRequest request) {
        return rezervasyonService.update(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        rezervasyonService.delete(id);
    }
}
