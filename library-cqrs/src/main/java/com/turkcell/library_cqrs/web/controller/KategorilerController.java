package com.turkcell.library_cqrs.web.controller;

import java.util.UUID;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.turkcell.library_cqrs.application.features.kategori.command.create.CreateKategoriCommand;
import com.turkcell.library_cqrs.core.mediator.Mediator;

@RequestMapping("/api/kategoriler")
@RestController
public class KategorilerController {
    private final Mediator mediator;

    public KategorilerController(Mediator mediator) {
        this.mediator = mediator;
    }

    @PostMapping
    public UUID create(@RequestBody CreateKategoriCommand command){
        return mediator.send(command);
    }
}
