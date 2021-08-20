package com.binance.bot.investing_com.prediction;

import com.binance.bot.binance.Coins;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.EnumMap;
import java.util.Optional;

class PredictionProviderTest {

    @Test
    public void test() {
        PredictionProviderService service = new PredictionProviderService();
        Optional<EnumMap<PredictionPeriods, PredictionCases>> predictions = service.getPredictions(Coins.XRP_USD);
        Assertions.assertTrue(predictions.isPresent());
        Assertions.assertEquals(5, predictions.get().size());
    }
}