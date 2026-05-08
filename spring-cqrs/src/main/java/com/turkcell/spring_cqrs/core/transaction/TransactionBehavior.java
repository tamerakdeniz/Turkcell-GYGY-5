package com.turkcell.spring_cqrs.core.transaction;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

import com.turkcell.spring_cqrs.core.mediator.cqrs.Command;
import com.turkcell.spring_cqrs.core.mediator.pipeline.PipelineBehavior;
import com.turkcell.spring_cqrs.core.mediator.pipeline.RequestHandlerDelegate;

// Transaction (İşlem) -> Bütünlük sağlama:
// Bir Command birden fazla DB değişikliği yapabilir (insert/update/delete).
// Hepsi başarılıysa COMMIT, herhangi biri hata fırlatırsa ROLLBACK.
// Böylece "yarım kalmış" veri tutarsız olmaz. (ACID -> Atomicity)
@Component
@Order(40)
public class TransactionBehavior implements PipelineBehavior {

    private final TransactionTemplate transactionTemplate;

    public TransactionBehavior(PlatformTransactionManager transactionManager) {
        this.transactionTemplate = new TransactionTemplate(transactionManager);
    }

    // Sadece Command'leri sarmalıyoruz; Query'ler okuma yaptığı için işlem gerekmiyor.
    @Override
    public boolean supports(Object request) {
        return request instanceof Command<?>;
    }

    @Override
    public <R> R handle(Object request, RequestHandlerDelegate<R> next) {
        String requestName = request.getClass().getSimpleName();
        System.out.println("[TX] BEGIN " + requestName);

        try {
            R result = transactionTemplate.execute(status -> next.invoke());
            System.out.println("[TX] COMMIT " + requestName);
            return result;
        } catch (RuntimeException ex) {
            System.out.println("[TX] ROLLBACK " + requestName + " -> " + ex.getMessage());
            throw ex;
        }
    }
}
