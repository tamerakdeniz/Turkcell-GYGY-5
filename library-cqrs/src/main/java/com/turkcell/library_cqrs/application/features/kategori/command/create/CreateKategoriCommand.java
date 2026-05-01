package com.turkcell.library_cqrs.application.features.kategori.command.create;

import java.util.UUID;

import com.turkcell.library_cqrs.core.mediator.cqrs.Command;

// Command-Query -> DTO
public record CreateKategoriCommand(String ad, String aciklama) implements Command<UUID> {}
