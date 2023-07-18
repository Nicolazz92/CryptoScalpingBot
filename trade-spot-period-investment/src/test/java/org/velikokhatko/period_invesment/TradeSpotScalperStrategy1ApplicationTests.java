package org.velikokhatko.period_invesment;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.velikokhatko.period_invesment.services.api.provider.AbstractBinanceApiProvider;
import org.velikokhatko.period_invesment.services.predictions.PredictionService;

class TradeSpotScalperStrategy1ApplicationTests extends BaseStrategy1Test {

    @Autowired
    private AbstractBinanceApiProvider apiProvider;
    @Autowired
    private PredictionService predictionService;

    @Test
    void getBalanceTest() {
        System.out.println(apiProvider.getBalance());
    }

    @Test
    void getPrediction() {
        final boolean canBuy = predictionService.canBuy("XRPBUSD");
        System.out.println(canBuy);
    }
}
