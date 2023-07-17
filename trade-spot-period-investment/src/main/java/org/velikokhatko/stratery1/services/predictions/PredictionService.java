package org.velikokhatko.stratery1.services.predictions;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.velikokhatko.stratery1.exceptions.TraderBotRuntimeException;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.concurrent.ExecutorService;

import static org.velikokhatko.stratery1.services.AppContext.PREDICTION_CACHE;

@Slf4j
@Service
public class PredictionService {

    private ScrappingService scrappingService;
    private ExecutorService executorService;

    /**
     * Пример: predictionService.canBuy("XRPBUSD");
     * Даёт прогноз по торговой паре
     *
     * @param symbol валютная пара
     * @return можно ли покупать
     */
    public boolean canBuy(String symbol) {
        if (symbol == null) {
            throw new TraderBotRuntimeException("symbol is null");
        }
        if (!PREDICTION_CACHE.containsKey(symbol) || PREDICTION_CACHE.get(symbol).freshLimit.isBefore(LocalDateTime.now())) {
            PREDICTION_CACHE.remove(symbol);
            executorService.execute(() -> {
                Optional<Prediction> predictionOptional = scrappingService.getPrediction(symbol);
                predictionOptional.ifPresent(prediction -> {
                    log.info("Для пары {} получено предсказание: {}", symbol, predictionOptional);
                    PREDICTION_CACHE.put(symbol, predictionOptional.get());
                });
            });
            return false;
        }

        return PREDICTION_CACHE.get(symbol).canBuy;
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
