package org.velikokhatko.stratery1.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Configuration
public class AppConfig {

    @Bean
    public ExecutorService executorService(@Value("${threadPoolSize}") int threadPoolSize) {
        return Executors.newFixedThreadPool(threadPoolSize);
    }
}
