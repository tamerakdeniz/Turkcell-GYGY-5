package com.turkcell.library_cqrs.core.mediator.cqrs;

public interface QueryHandler<Q extends Query<R>, R> { // Q => Query, R => Dönüş Tipi (Dinamik olarak belirlenebilir)

    R handle(Q query);

}
