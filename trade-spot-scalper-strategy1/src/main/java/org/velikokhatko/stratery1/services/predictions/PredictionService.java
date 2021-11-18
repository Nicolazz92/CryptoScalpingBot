package org.velikokhatko.stratery1.services.predictions;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
public class PredictionService {

    private final Map<String, Prediction> predictionCache = new HashMap<>();
    private ScrappingService scrappingService;

    public boolean canBuy(String symbol) {
        if (!predictionCache.containsKey(symbol)
                || predictionCache.get(symbol).freshLimit.isBefore(LocalDateTime.now())) {
            predictionCache.put(symbol, scrappingService.getPrediction(symbol));
        }

        return predictionCache.get(symbol).canBuy;
    }

    @Autowired
    public void setScrappingService(ScrappingService scrappingService) {
        this.scrappingService = scrappingService;
    }
}
