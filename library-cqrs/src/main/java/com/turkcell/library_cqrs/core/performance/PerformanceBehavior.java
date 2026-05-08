package com.turkcell.library_cqrs.core.performance;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.turkcell.library_cqrs.core.mediator.pipeline.PipelineBehavior;
import com.turkcell.library_cqrs.core.mediator.pipeline.RequestHandlerDelegate;

@Component
@Order(30)
public class PerformanceBehavior implements PipelineBehavior {

    private static final long THRESHOLD_MS = 3000L;

    @Override
    public <R> R handle(Object request, RequestHandlerDelegate<R> next) {
        String requestName = request.getClass().getSimpleName();
        long start = System.currentTimeMillis();

        try {
            return next.invoke();
        } finally {
            long elapsed = System.currentTimeMillis() - start;
            if (elapsed > THRESHOLD_MS) {
                System.out.println(
                    "[PERF][WARN] " + requestName + " yavaş çalıştı: " + elapsed + " ms (eşik: " + THRESHOLD_MS + " ms)");
            }
        }
    }

}
