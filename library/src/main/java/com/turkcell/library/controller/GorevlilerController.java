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

import com.turkcell.library.dto.gorevli.CreateGorevliRequest;
import com.turkcell.library.dto.gorevli.GorevliResponse;
import com.turkcell.library.dto.gorevli.UpdateGorevliRequest;
import com.turkcell.library.service.GorevliServiceImpl;

@RestController
@RequestMapping("/api/gorevliler")
public class GorevlilerController {

    private final GorevliServiceImpl gorevliService;

    public GorevlilerController(GorevliServiceImpl gorevliService) {
        this.gorevliService = gorevliService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public GorevliResponse create(@RequestBody CreateGorevliRequest request) {
        return gorevliService.create(request);
    }

    @GetMapping
    public List<GorevliResponse> getAll() {
        return gorevliService.getAll();
    }

    @GetMapping("/{id}")
    public GorevliResponse getById(@PathVariable Long id) {
        return gorevliService.getById(id);
    }

    @PutMapping("/{id}")
    public GorevliResponse update(@PathVariable Long id, @RequestBody UpdateGorevliRequest request) {
        return gorevliService.update(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        gorevliService.delete(id);
    }
}
