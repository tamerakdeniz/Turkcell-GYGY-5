package com.turkcell.library_cqrs.core.mediator;

import com.turkcell.library_cqrs.core.mediator.cqrs.Query;
import com.turkcell.library_cqrs.core.mediator.cqrs.Command;

public interface Mediator {
    <R> R send(Command<R> request);
    <R> R send(Query<R> command);
}

// CreateKategoriCommand - CreateKategoriCommandHandler
// CreateKategoriCommandHandler implements CommandHandler<CreateKategoriCommand, CreateKategoriResponse>
