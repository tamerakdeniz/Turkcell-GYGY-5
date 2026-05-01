package com.turkcell.library_cqrs.core.mediator;

import java.util.HashMap;
import java.util.Map;

import org.springframework.context.ApplicationContext;
import org.springframework.core.GenericTypeResolver;
import org.springframework.stereotype.Component;

import com.turkcell.library_cqrs.core.mediator.cqrs.Command;
import com.turkcell.library_cqrs.core.mediator.cqrs.CommandHandler;
import com.turkcell.library_cqrs.core.mediator.cqrs.Query;
import com.turkcell.library_cqrs.core.mediator.cqrs.QueryHandler;

import jakarta.annotation.PostConstruct;

// Hangi command/query -> hangi handler?
// Hangi command/query geldiğine göre ilgili handler'ı çözmek için bir yöntem ekleyelim

@Component
public class SpringMediator implements Mediator
{
    private final Map<Class<?>, CommandHandler<?, ?>> commandHandlers = new HashMap<>();
    private final Map<Class<?>, QueryHandler<?, ?>> queryHandlers = new HashMap<>();

    private final ApplicationContext context;

    public SpringMediator(ApplicationContext context) {
        this.context = context;
    }

    @PostConstruct
    @SuppressWarnings("rawtypes")
    private void registerHandlers() {
        context.getBeansOfType(CommandHandler.class).values().forEach(handler -> {
            Class<?> requestType = resolveRequestType(handler.getClass(), CommandHandler.class);
            CommandHandler<?, ?> existing = commandHandlers.putIfAbsent(requestType, handler);
            if (existing != null) {
                throw new IllegalStateException(
                    "Birden fazla CommandHandler bulundu: " + requestType.getSimpleName());
            }
        });

        context.getBeansOfType(QueryHandler.class).values().forEach(handler -> {
            Class<?> requestType = resolveRequestType(handler.getClass(), QueryHandler.class);
            QueryHandler<?, ?> existing = queryHandlers.putIfAbsent(requestType, handler);
            if (existing != null) {
                throw new IllegalStateException(
                    "Birden fazla QueryHandler bulundu: " + requestType.getSimpleName());
            }
        });
    }

    @Override
    @SuppressWarnings("unchecked")
    public <R> R send(Command<R> command) {
        CommandHandler<Command<R>, R> handler =
            (CommandHandler<Command<R>, R>) commandHandlers.get(command.getClass());

        if (handler == null)
            throw new IllegalStateException("Handler bulunamadı: " + command.getClass().getSimpleName());

        return handler.handle(command);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <R> R send(Query<R> query) {
        QueryHandler<Query<R>, R> handler =
            (QueryHandler<Query<R>, R>) queryHandlers.get(query.getClass());

        if (handler == null)
            throw new IllegalStateException("Handler bulunamadı: " + query.getClass().getSimpleName());

        return handler.handle(query);
    }

    private Class<?> resolveRequestType(Class<?> handlerClass, Class<?> handlerInterface) {
        Class<?>[] generics = GenericTypeResolver.resolveTypeArguments(handlerClass, handlerInterface);
        if (generics == null || generics.length == 0 || generics[0] == null)
            throw new IllegalStateException(
                "Handler için generic tip çözümlenemedi: " + handlerClass.getName());
        return generics[0];
    }
}
