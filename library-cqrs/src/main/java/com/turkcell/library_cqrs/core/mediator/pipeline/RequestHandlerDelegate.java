package com.turkcell.library_cqrs.core.mediator.pipeline;

@FunctionalInterface
public interface RequestHandlerDelegate<R> {
    R invoke();
}
