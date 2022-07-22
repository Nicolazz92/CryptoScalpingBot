package org.velikokhatko.stratery1.services.api.exchange;

import com.binance.api.client.domain.general.SymbolInfo;
import com.binance.api.client.domain.general.SymbolStatus;
import com.velikokhatko.model.SymbolInfoShort;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.velikokhatko.stratery1.services.api.custom.domain.CoinInfo;
import org.velikokhatko.stratery1.services.api.provider.AbstractBinanceApiProvider;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.stream.Collectors;

import static org.velikokhatko.stratery1.utils.Utils.extractSymbolInfoShort;

@Slf4j
@Service
public class ExchangeInfoService {

    private final Map<String, SymbolInfoShort> symbolInfoShortCache = new ConcurrentHashMap<>();
    private final Map<String, CoinInfo> coinInfoCache = new ConcurrentHashMap<>();
    private AbstractBinanceApiProvider apiProvider;
    private ExecutorService executorService;
    private String bridgeCoin;

    @Scheduled(cron = "@monthly")
    public void clearCache() {
        symbolInfoShortCache.clear();
        coinInfoCache.clear();
    }

    public Optional<String> getCoinFullName(String coinMemo) {
        return coinInfoCache.containsKey(coinMemo)
                ? Optional.of(coinInfoCache.get(coinMemo).getName())
                : Optional.empty();
    }

    public Optional<String> getBaseAsset(String symbol) {
        if (!symbolInfoShortCache.containsKey(symbol)) {
            log.error("Не получилось найти базовый актив для " + symbol);
            executorService.execute(this::warmUpCache);
            return Optional.empty();
        }
        return Optional.ofNullable(symbolInfoShortCache.get(symbol).getBaseAsset());
    }

    public Map<String, SymbolInfoShort> getAllSymbolInfoShort() {
        if (symbolInfoShortCache.isEmpty()) {
            executorService.execute(this::warmUpCache);
            return Collections.emptyMap();
        }
        return symbolInfoShortCache;
    }

    private void warmUpCache() {
        final List<SymbolInfo> symbols = apiProvider.getExchangeInfo().getSymbols().stream()
                .filter(symbolInfo -> SymbolStatus.TRADING == symbolInfo.getStatus())
                .filter(SymbolInfo::isSpotTradingAllowed)
                .filter(symbolInfo -> bridgeCoin.equals(symbolInfo.getQuoteAsset()))
                .peek(symbolInfo -> symbolInfoShortCache.put(symbolInfo.getSymbol(), extractSymbolInfoShort(symbolInfo)))
                .collect(Collectors.toList());
        log.info("Была получена информация о {} торговых парах: {}",
                symbols.size(), symbols.stream().map(SymbolInfo::getSymbol).collect(Collectors.joining(", ")));
        if (symbolInfoShortCache.isEmpty() && !symbols.isEmpty()) {
            log.error("Не удалось заполнить symbolInfoShortCache, возможно, bridgeCoin задана с ошибкой");
        }
        apiProvider.getAllCoinsInfo().forEach(coinInfo -> coinInfoCache.put(coinInfo.getCoin(), coinInfo));
        if (coinInfoCache.isEmpty()) {
            log.error("Не удалось заполнить coinInfoCache");
        }
    }

    @Autowired
    public void setApiProvider(AbstractBinanceApiProvider apiProvider) {
        this.apiProvider = apiProvider;
    }

    @Autowired
    public void setExecutorService(ExecutorService executorService) {
        this.executorService = executorService;
    }

    @Value("${bridgeCoin}")
    public void setBridgeCoin(String bridgeCoin) {
        Assert.hasText(bridgeCoin, "Не задана bridgeCoin");
        this.bridgeCoin = bridgeCoin;
    }
}
