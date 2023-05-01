package com.h12.ecommerce.configurations;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
@EnableAsync
public class AsyncConfiguration {
    @Bean(name = "asyncExecutor")
    public Executor asyncExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(3);
        executor.setMaxPoolSize(3);
        executor.setQueueCapacity(100);
        executor.setThreadNamePrefix("AsyncThread-");
        executor.initialize();

        ExecutorService executorService;

        // It's a best practice utilize the maximum core capacity
        // until there is a valid reason to use the lesser value
        executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());


//        try {
//            CompletableFuture<Long> completableFuture = CompletableFuture.supplyAsync(() -> helperForExecution(100L));
//            while (!completableFuture.isDone()) {
//                System.out.println("CompletableFuture is not finished yet...");
//            }
//            long result = completableFuture.get();
//        } catch (ExecutionException | InterruptedException e) {
//            throw new RuntimeException(e);
//        }
        return executor;
    }

    private <U> U helperForExecution(Long longValue) {
        return null;
    }
}