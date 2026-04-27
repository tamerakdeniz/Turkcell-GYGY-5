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

import com.turkcell.library.dto.iade.CreateIadeRequest;
import com.turkcell.library.dto.iade.IadeResponse;
import com.turkcell.library.dto.iade.UpdateIadeRequest;
import com.turkcell.library.service.IadeServiceImpl;

@RestController
@RequestMapping("/api/iadeler")
public class IadelerController {

    private final IadeServiceImpl iadeService;

    public IadelerController(IadeServiceImpl iadeService) {
        this.iadeService = iadeService;
    }

    @PostMapping
    public IadeResponse create(@RequestBody CreateIadeRequest request) {
        return iadeService.create(request);
    }

    @GetMapping
    public List<IadeResponse> getAll() {
        return iadeService.getAll();
    }

    @GetMapping("/{id}")
    public IadeResponse getById(@PathVariable Long id) {
        return iadeService.getById(id);
    }

    @PutMapping("/{id}")
    public IadeResponse update(@PathVariable Long id, @RequestBody UpdateIadeRequest request) {
        return iadeService.update(id, request);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        iadeService.delete(id);
    }
}
