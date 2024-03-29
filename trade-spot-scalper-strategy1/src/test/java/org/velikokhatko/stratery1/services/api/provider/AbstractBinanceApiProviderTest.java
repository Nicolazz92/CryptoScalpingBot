package org.velikokhatko.stratery1.services.api.provider;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.util.Assert;
import org.velikokhatko.stratery1.BaseStrategy1Test;
import org.velikokhatko.stratery1.services.api.custom.domain.CoinInfo;

import java.util.List;

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

    /**
     * Не работает в testnet
     */
    @Test
    void getAllCoinsInfoTest() {
        List<CoinInfo> allCoinsInfo = binanceApiProvider.getAllCoinsInfo();
        Assert.notNull(allCoinsInfo, "getAllCoinsInfo вернул null");
        Assert.notEmpty(allCoinsInfo, "getAllCoinsInfo пустой");
    }
}