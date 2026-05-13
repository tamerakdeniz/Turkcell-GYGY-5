package com.turkcell.spring_cqrs.core.security.authorization;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.turkcell.spring_cqrs.core.mediator.pipeline.PipelineBehavior;
import com.turkcell.spring_cqrs.core.mediator.pipeline.RequestHandlerDelegate;
import com.turkcell.spring_cqrs.core.security.context.UserContext;

@Component
@Order(10)
public class AuthorizationBehavior implements PipelineBehavior {
    private final UserContext userContext;

    public AuthorizationBehavior(UserContext userContext) {
        this.userContext = userContext;
    }

    @Override
    public boolean supports(Object request) {
        return request instanceof AuthorizableRequest;
    }

    // ilgili handler'ın öncesi ve sonrası çalıştırabilen kodlar.
    @Override
    public <R> R handle(Object request, RequestHandlerDelegate<R> next) {

        if(!userContext.isAuthenticated())
            throw new RuntimeException("Giriş yapmalısın..");

        // Özel bir exception türü belirle.. 
        // Handlerda bu exceptionu eğer giriş yapılmamısa 401, (unauthenticatedException) eğer giriş yapılmış
        //  ama yetkisi (rol yetersizse) yoksa 403 (UnauthorizedException) dönecek şekilde güncelle.

        return next.invoke(); // zincirdeki sonraki halkayı çağır..
    }

}