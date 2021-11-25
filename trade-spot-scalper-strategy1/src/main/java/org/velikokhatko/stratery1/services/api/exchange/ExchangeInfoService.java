package org.velikokhatko.stratery1.services.api.exchange;

import com.binance.api.client.domain.general.SymbolInfo;
import com.binance.api.client.domain.general.SymbolStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.velikokhatko.stratery1.services.api.provider.AbstractBinanceApiProvider;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class ExchangeInfoService {

    private final Map<String, SymbolInfoShort> cache = new HashMap<>();
    private AbstractBinanceApiProvider apiProvider;
    private String bridgeCoin;

    public String getBaseAsset(String symbol) {
        if (!cache.containsKey(symbol)) {
            warmUpCache();
        }
        if (!cache.containsKey(symbol)) {
            log.error("Не получилось найти базовый актив для " + symbol);
        }
        return cache.get(symbol).getBaseAsset();
    }

    private void warmUpCache() {
        final List<SymbolInfo> symbols = apiProvider.getExchangeInfo().getSymbols();
        symbols.stream()
                .filter(symbolInfo -> SymbolStatus.TRADING == symbolInfo.getStatus())
                .filter(SymbolInfo::isSpotTradingAllowed)
                .filter(symbolInfo -> bridgeCoin.equals(symbolInfo.getQuoteAsset()))
                .forEach(symbolInfo -> cache.put(symbolInfo.getSymbol(), new SymbolInfoShort(symbolInfo)));
        if (cache.isEmpty() && !symbols.isEmpty()) {
            log.error("Не удалось заполнить кэш, возможно, bridgeCoin задана с ошибкой");
        }
    }

    @Scheduled(cron = "0 0 0 * * ?")
    public void clearCache() {
        cache.clear();
        log.info(this.getClass().getName() + ": cache cleaned");
    }

    @Autowired
    public void setApiProvider(AbstractBinanceApiProvider apiProvider) {
        this.apiProvider = apiProvider;
    }

    @Value("${bridgeCoin}")
    public void setBridgeCoin(String bridgeCoin) {
        Assert.hasText(bridgeCoin, "Не задана bridgeCoin");
        this.bridgeCoin = bridgeCoin;
    }
}
