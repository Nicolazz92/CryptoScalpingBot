package org.velikokhatko.period_invesment.services.api.provider;

import com.binance.api.client.domain.general.ExchangeInfo;
import com.binance.api.client.domain.general.SymbolInfo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.velikokhatko.period_invesment.BaseStrategy1Test;
import org.velikokhatko.period_invesment.services.api.custom.domain.CoinInfo;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

    @Test
    void testGetExchangeInfo() {
        ExchangeInfo exchangeInfo = binanceApiProvider.getExchangeInfo();
        Assert.notNull(exchangeInfo, "getExchangeInfo вернул null");

        Map<String, List<SymbolInfo>> collect = binanceApiProvider.getExchangeInfo()
                .getSymbols()
                .stream().collect(Collectors.groupingBy(SymbolInfo::getBaseAsset));

        System.out.printf("");
    }
}