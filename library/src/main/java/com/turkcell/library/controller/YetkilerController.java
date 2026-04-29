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

import com.turkcell.library.dto.yetki.CreateYetkiRequest;
import com.turkcell.library.dto.yetki.UpdateYetkiRequest;
import com.turkcell.library.dto.yetki.YetkiResponse;
import com.turkcell.library.service.YetkiServiceImpl;

@RestController
@RequestMapping("/api/yetkiler")
public class YetkilerController {

    private final YetkiServiceImpl yetkiService;

    public YetkilerController(YetkiServiceImpl yetkiService) {
        this.yetkiService = yetkiService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public YetkiResponse create(@RequestBody CreateYetkiRequest request) {
        return yetkiService.create(request);
    }

    @GetMapping
    public List<YetkiResponse> getAll() {
        return yetkiService.getAll();
    }

    @GetMapping("/{id}")
    public YetkiResponse getById(@PathVariable Long id) {
        return yetkiService.getById(id);
    }

    @PutMapping("/{id}")
    public YetkiResponse update(@PathVariable Long id, @RequestBody UpdateYetkiRequest request) {
        return yetkiService.update(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        yetkiService.delete(id);
    }
}
