package com.turkcell.spring_cqrs.core.mediator.cqrs;

public interface CommandHandler<C extends Command<R>, R> { // C => Command, R => Dönüş Tipi (Dinamik olarak belirlenebilir)

    R handle(C command);

}
