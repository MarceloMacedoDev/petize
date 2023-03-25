package br.com.petize.aplication.events;

import io.github.resilience4j.retry.Retry;
import io.github.resilience4j.retry.RetryConfig;
import io.github.resilience4j.retry.RetryRegistry;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.time.Duration;
import java.util.function.Supplier;

@RequiredArgsConstructor
@Component
public class RetryComponent {
    private final RetryRegistry retryRegistry;


    public <T> T executeWithRetry(String retryName, Supplier<T> supplier) {
        Retry retry = retryRegistry.retry(retryName);
        return Retry.decorateSupplier(retry, supplier).get();
    }

    public <T> T executeWithRetry(Supplier<T> supplier) {
        return executeWithRetry("retryDefault", supplier);
    }

    // getters e setters
   /* @PostConstruct
    public void init() {
        RetryConfig config = RetryConfig.custom()
                .maxAttempts(15)
                .waitDuration(Duration.ofMillis(500))
                .build();
        retryRegistry.addConfiguration("retryDefault", config);
    }*/
}

