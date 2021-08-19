package com.binance.bot.investing_com.prediction;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.EnumMap;
import java.util.Optional;

class PredictionProviderTest {

    @Test
    public void test() {
        String XRP_USD = "https://www.investing.com/crypto/xrp/xrp-usd?cid=1075586";

        PredictionProviderService service = new PredictionProviderService();
        Optional<EnumMap<PredictionPeriods, PredictionCases>> predictions = service.getPredictions(XRP_USD);
        Assertions.assertTrue(predictions.isPresent());
        Assertions.assertEquals(5, predictions.get().size());
    }
}