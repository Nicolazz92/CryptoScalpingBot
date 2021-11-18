package org.velikokhatko.stratery1.services.predictions;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
public class PredictionService {

    private final Map<String, Prediction> cache = new HashMap<>();
    private ScrappingService scrappingService;

    /**
     * Пример: predictionService.canBuy("XRPBUSD");
     * Даёт прогноз по вылютной паре, основываясь на базовом активе (левом)
     *
     * @param symbol валютная пара
     * @return можно ли покупать
     */
    public boolean canBuy(String symbol) {
        if (!cache.containsKey(symbol)
                || cache.get(symbol).freshLimit.isBefore(LocalDateTime.now())) {
            cache.put(symbol, scrappingService.getPrediction(symbol));
        }

        return cache.get(symbol).canBuy;
    }

    @Scheduled(cron = "0 0 12 1 * ?")
    public void clearCache() {
        cache.clear();
        log.info(this.getClass().getName() + ": cache cleaned");
    }

    @Autowired
    public void setScrappingService(ScrappingService scrappingService) {
        this.scrappingService = scrappingService;
    }
}
