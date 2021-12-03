package org.velikokhatko.stratery1.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Configuration
@EnableScheduling
public class AppConfig {

    @Bean
    public ExecutorService scheduledExecutorService(@Value("${threadPoolSize}") int threadPoolSize) {
        return Executors.newCachedThreadPool();
    }
}
