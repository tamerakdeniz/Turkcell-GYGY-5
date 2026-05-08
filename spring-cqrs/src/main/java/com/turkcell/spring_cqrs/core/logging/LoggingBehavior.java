package com.turkcell.spring_cqrs.core.logging;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.turkcell.spring_cqrs.core.mediator.pipeline.PipelineBehavior;
import com.turkcell.spring_cqrs.core.mediator.pipeline.RequestHandlerDelegate;

@Component
@Order(20)
public class LoggingBehavior implements PipelineBehavior {

    // sadece şunları destekler:
    @Override
    public boolean supports(Object request) {
       return !(request instanceof NotLoggableRequest); // eğer ilgili request (command/query) NotLoggableRequest ile imzalanmamış ise yap.
    }

    @Override
    public <R> R handle(Object request, RequestHandlerDelegate<R> next) {
        String requestName = request.getClass().getSimpleName();

        System.out.println("[LOG][REQUEST] " + requestName + " -> " + request);

        R response = next.invoke();

        System.out.println("[LOG][RESPONSE] " + requestName + " -> " + response);

        return response;
    }

}
