package org.velikokhatko.stratery1.services.api.provider;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.velikokhatko.stratery1.BaseStrategy1Test;

class AbstractBinanceApiProviderTest extends BaseStrategy1Test {

    @Autowired
    private AbstractBinanceApiProvider binanceApiProvider;

    @Test
    void getFreeBridgeUSDBalance() {
        double freeBridgeUSDBalance = binanceApiProvider.getFreeBridgeCoinUSDBalance();
        System.out.println(freeBridgeUSDBalance);
    }

    @Test
    void getBalance() {
    }

    @Test
    void getExchangeInfo() {
    }
}