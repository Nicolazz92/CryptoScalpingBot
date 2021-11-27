package org.velikokhatko.stratery1.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

@Configuration
public class AppConfig {

    @Bean
    public ExecutorService executorServiceFixedSize(@Value("${threadPoolSize}") int threadPoolSize) {
        return Executors.newFixedThreadPool(threadPoolSize);
    }

    @Bean
    public ScheduledExecutorService scheduledExecutorService() {
        return Executors.newScheduledThreadPool(2);
    }
}
