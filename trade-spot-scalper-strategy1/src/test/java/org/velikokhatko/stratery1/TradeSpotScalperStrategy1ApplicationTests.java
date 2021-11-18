package org.velikokhatko.stratery1;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.velikokhatko.stratery1.services.api.provider.AbstractBinanceApiProvider;
import org.velikokhatko.stratery1.services.predictions.PredictionService;

@SpringBootTest
//@ActiveProfiles("production")
@ActiveProfiles("testnet")
class TradeSpotScalperStrategy1ApplicationTests {

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
