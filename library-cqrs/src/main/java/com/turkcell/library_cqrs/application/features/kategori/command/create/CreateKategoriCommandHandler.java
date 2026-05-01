package com.turkcell.library_cqrs.application.features.kategori.command.create;

import java.util.UUID;

import org.springframework.stereotype.Component;

import com.turkcell.library_cqrs.core.mediator.cqrs.CommandHandler;

@Component
public class CreateKategoriCommandHandler implements CommandHandler<CreateKategoriCommand, UUID> {

    @Override
    public UUID handle(CreateKategoriCommand command) {
        System.out.println("Create command çalıştı");
        return UUID.randomUUID();
    }

}
