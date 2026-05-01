package com.turkcell.spring_cqrs.core.mediator;

import org.springframework.context.ApplicationContext;
import org.springframework.core.ResolvableType;
import org.springframework.stereotype.Component;

import com.turkcell.spring_cqrs.core.mediator.cqrs.Command;
import com.turkcell.spring_cqrs.core.mediator.cqrs.CommandHandler;
import com.turkcell.spring_cqrs.core.mediator.cqrs.Query;
import com.turkcell.spring_cqrs.core.mediator.cqrs.QueryHandler;

@Component
public class SpringMediator implements Mediator
{
    private final ApplicationContext context;
    public SpringMediator(ApplicationContext context) {
        this.context = context;
    }

    @Override
    public <R> R send(Command<R> command) {
        var handler = (CommandHandler<Command<R>, R>) resolveHandler(command.getClass(), CommandHandler.class);

        return handler.handle(command);
    }

    @Override
    public <R> R send(Query<R> query) {
        var handler = (QueryHandler<Query<R>, R>) resolveHandler(query.getClass(), QueryHandler.class);

        return handler.handle(query);
    }

    // Hangi command/query -> hangi handler?
    // Hangi command/query geldiğine göre ilgili handler'ı çözmek için bir yöntem ekleyelim

    private Object resolveHandler(Class<?> requestType, Class<?> handlerInterface) {
        // TODO: Refactor & Add Caching

        // Tüm handlerları gez, komutla/query ile uyuşanı dön.
        String[] beanNames = context.getBeanNamesForType(handlerInterface);

        for(String beanName: beanNames)
        {
            Class<?> beanClass = context.getType(beanName);
            if(beanClass == null) continue;

            ResolvableType[] interfaces = ResolvableType.forClass(beanClass).getInterfaces();

            for(ResolvableType iface: interfaces)
            {
                if(iface.getRawClass() != null && handlerInterface.isAssignableFrom(iface.getRawClass()))
                {
                    Class<?> firstGeneric = iface.getGeneric(0).resolve();

                    if(firstGeneric != null && firstGeneric.equals(requestType))
                        return context.getBean(beanName);
                }
            }
        }
        throw new IllegalStateException("Handler bulunamadı." + requestType.getSimpleName());
    }

}

