package org.velikokhatko.stratery1.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Configuration
@EnableScheduling
public class AppConfig {

    @Bean
    public ExecutorService executorService(@Value("${threadPoolSize}") int threadPoolSize) {
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(
                threadPoolSize,
                threadPoolSize,
                5,
                TimeUnit.MINUTES,
                new LinkedBlockingQueue<>()
        );
        threadPoolExecutor.allowCoreThreadTimeOut(true);
        return threadPoolExecutor;
    }
}
