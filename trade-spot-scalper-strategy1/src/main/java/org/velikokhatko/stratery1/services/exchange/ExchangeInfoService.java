package org.velikokhatko.stratery1.services.exchange;

import com.binance.api.client.domain.general.SymbolInfo;
import com.binance.api.client.domain.general.SymbolStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.velikokhatko.stratery1.constants.UsdStablecoins;
import org.velikokhatko.stratery1.services.api.provider.AbstractBinanceApiProvider;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
public class ExchangeInfoService {

    private final Map<String, SymbolInfoShort> cache = new HashMap<>();
    private AbstractBinanceApiProvider apiProvider;
    private UsdStablecoins bridgeCoin;

    @Scheduled(cron = "0 0 0 * * ?")
    public void clearCache() {
        cache.clear();
        log.info(this.getClass().getName() + ": cache cleaned");
    }

    public String getBaseAsset(String symbol) {
        if (!cache.containsKey(symbol)) {
            apiProvider.getExchangeInfo().getSymbols().stream()
                    .filter(symbolInfo -> SymbolStatus.TRADING == symbolInfo.getStatus())
                    .filter(SymbolInfo::isSpotTradingAllowed)
                    .filter(symbolInfo -> bridgeCoin.name().equals(symbolInfo.getQuoteAsset()))
                    .forEach(symbolInfo -> cache.put(symbolInfo.getSymbol(), new SymbolInfoShort(symbolInfo)));
        }
        if (!cache.containsKey(symbol)) {
            log.error("Не получилось найти базовый актив для " + symbol);
        }
        return cache.get(symbol).baseAsset;
    }

    @Autowired
    public void setApiProvider(AbstractBinanceApiProvider apiProvider) {
        this.apiProvider = apiProvider;
    }

    @Value("${bridgeCoin}")
    public void setBridgeCoin(UsdStablecoins bridgeCoin) {
        this.bridgeCoin = bridgeCoin;
    }
}
