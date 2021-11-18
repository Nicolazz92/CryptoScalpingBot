package org.velikokhatko.stratery1.services.exchange;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.velikokhatko.stratery1.services.api.provider.AbstractBinanceApiProvider;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
public class ExchangeInfoService {

    private AbstractBinanceApiProvider apiProvider;
    private final Map<String, SymbolInfoShort> cache = new HashMap<>();

    @Scheduled(cron = "0 0 0 * * ?")
    public void clearCache() {
        cache.clear();
        log.info(this.getClass().getName() + ": cache cleaned");
    }

    public String getBaseAsset(String symbol) {
        if (!cache.containsKey(symbol)) {
            apiProvider.getExchangeInfo()
                    .getSymbols()
//                    .stream().peek(s -> System.out.println(s.toString()))
                    .forEach(s -> cache.put(s.getSymbol(), new SymbolInfoShort(s)));
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
}
