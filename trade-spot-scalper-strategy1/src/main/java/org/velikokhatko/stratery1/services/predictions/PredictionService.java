package org.velikokhatko.stratery1.services.predictions;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;

@Slf4j
@Service
public class PredictionService {

    private final Map<String, Prediction> cache = new ConcurrentHashMap<>();
    private ScrappingService scrappingService;
    private ExecutorService executorService;

    /**
     * Пример: predictionService.canBuy("XRPBUSD");
     * Даёт прогноз по вылютной паре
     *
     * @param symbol валютная пара
     * @return можно ли покупать
     */
    public boolean canBuy(String symbol) {
        if (!cache.containsKey(symbol) || cache.get(symbol).freshLimit.isBefore(LocalDateTime.now())) {
            executorService.execute(() -> {
                Prediction prediction = scrappingService.getPrediction(symbol);
                log.info("Для валютной пары получено предсказание: {}", prediction);
                cache.put(symbol, prediction);
            });
            return false;
        }

        return cache.get(symbol).canBuy;
    }

    @Autowired
    public void setScrappingService(ScrappingService scrappingService) {
        this.scrappingService = scrappingService;
    }

    @Autowired
    public void setExecutorService(ExecutorService executorService) {
        this.executorService = executorService;
    }
}
