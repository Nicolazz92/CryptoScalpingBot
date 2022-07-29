package org.velikokhatko.stratery1.services.trade;

import com.binance.api.client.domain.market.TickerPrice;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.velikokhatko.stratery1.services.api.exchange.ExchangeInfoService;
import org.velikokhatko.stratery1.services.api.provider.AbstractBinanceApiProvider;
import org.velikokhatko.stratery1.services.predictions.PredictionService;
import org.velikokhatko.stratery1.services.ratio.SingleCoinRatioSelectingService;
import velikokhatko.dto.RatioParamsDTO;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.stream.Collectors;

import static org.velikokhatko.stratery1.utils.Utils.truncate;

@Slf4j
public abstract class AbstractTradingService {

    protected AbstractBinanceApiProvider binanceApiProvider;
    protected ExchangeInfoService exchangeInfoService;
    protected PredictionService predictionService;
    protected SingleCoinRatioSelectingService ratioSelectingService;
    private ExecutorService executorService;
    protected double orderLotUSDSize;
    protected int allPricesCacheSize;
    protected Map<LocalDateTime, Map<String, Double>> allPricesCache = new ConcurrentHashMap<>();
    private String bridgeCoin;
    private LocalDateTime healthMonitor = truncate(LocalDateTime.now());

    /**
     * ГЛАВНАЯ ФУНКЦИЯ
     */
    @Scheduled(cron = "10 * * * * *")
    public void trade() {
        healthMonitor = truncate(LocalDateTime.now());
        updateAllPricesCache(allPricesCache);

        double freeBridgeCoinUSDBalance = getFreeBridgeCoinUSDBalance();
        int availableOrderSlots = (int) (freeBridgeCoinUSDBalance / orderLotUSDSize);
        if (availableOrderSlots > 0) {
            List<RatioParamsDTO> ratioParamsPotentialOrders = exchangeInfoService.getAllSymbolInfoShort().keySet().stream()
                    .filter(predictionService::canBuy)
                    .filter(this::doesNotHolding)
                    .filter(this::isProfitableFall)
                    .map(ratioSelectingService::selectRatio)
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .sorted(Comparator.comparing(RatioParamsDTO::getDeltaPercent).reversed())
                    .limit(availableOrderSlots)
                    .collect(Collectors.toList());
            if (ratioParamsPotentialOrders.isEmpty()) {
                log.info("Не найдено условий для выставления ордеров, всего денег: {}$", countAllMoney());
            } else {
                ratioParamsPotentialOrders.forEach(rp -> {
                    log.info("Готово к выставлению ордера: {}", rp);
                    executorService.execute(() -> openLongPosition(rp));
                });
            }
        } else {
            log.info("Не хватает баланса {} для выставления ордера", bridgeCoin);
        }
    }

    protected void updateAllPricesCache(Map<LocalDateTime, Map<String, Double>> cache) {
        List<LocalDateTime> oldKeys = cache.keySet().stream()
                .filter(key -> key.isBefore(LocalDateTime.now().minusMinutes(allPricesCacheSize + 1)))
                .collect(Collectors.toList());
        oldKeys.forEach(cache::remove);
        Map<String, Double> currentPrices = binanceApiProvider.getAllPrices().stream()
                .collect(Collectors.toMap(
                        TickerPrice::getSymbol,
                        tickerPrice -> Double.valueOf(tickerPrice.getPrice()))
                );
        cache.put(truncate(LocalDateTime.now()), currentPrices);
    }

    protected abstract double getFreeBridgeCoinUSDBalance();

    abstract protected boolean doesNotHolding(String s);

    private boolean isProfitableFall(String symbol) {
        Optional<RatioParamsDTO> ratioParamsOptional = ratioSelectingService.selectRatio(symbol);
        if (ratioParamsOptional.isPresent()) {
            RatioParamsDTO ratioParams = ratioParamsOptional.get();
            LocalDateTime currentPriceKey = truncate(LocalDateTime.now());
            LocalDateTime oldPriceKey = truncate(LocalDateTime.now().minusMinutes(ratioParams.getDeltaMinuteInterval()));

            Map<String, Double> currentPrices = allPricesCache.get(currentPriceKey);
            Map<String, Double> oldPrices = allPricesCache.get(oldPriceKey);

            if (oldPrices != null && oldPrices.get(symbol) != null
                    && currentPrices != null && currentPrices.get(symbol) != null) {
                Double oldPrice = oldPrices.get(symbol);
                Double currentPrice = currentPrices.get(symbol);
                return oldPrice > currentPrice
                        && 100d - (currentPrice / oldPrice) * 100 >= ratioParams.getDeltaPercent();
            } else {
                if (currentPrices == null || currentPrices.get(symbol) == null) {
                    log.warn("Не найдены текущие значения цены для символа {} по ключу {}", symbol, currentPriceKey);
                }
                if (oldPrices == null || oldPrices.get(symbol) == null) {
                    log.warn("Не найдены старые значения цены для символа {} по ключу {}", symbol, oldPriceKey);
                }
            }
        }

        return false;
    }

    abstract protected void openLongPosition(RatioParamsDTO ratioParams);

    abstract public double countAllMoney();

    public LocalDateTime getHealthMonitor() {
        return healthMonitor;
    }

    @Autowired
    public void setBinanceApiProvider(AbstractBinanceApiProvider binanceApiProvider) {
        this.binanceApiProvider = binanceApiProvider;
    }

    @Autowired
    public void setExchangeInfoService(ExchangeInfoService exchangeInfoService) {
        this.exchangeInfoService = exchangeInfoService;
    }

    @Autowired
    public void setPredictionService(PredictionService predictionService) {
        this.predictionService = predictionService;
    }

    @Autowired
    public void setRatioSelectingService(SingleCoinRatioSelectingService ratioSelectingService) {
        this.ratioSelectingService = ratioSelectingService;
    }

    @Autowired
    public void setExecutorService(ExecutorService executorService) {
        this.executorService = executorService;
    }

    @Value("${orderLotUSDSize}")
    public void setOrderLotUSDSize(String orderLotUSDSize) {
        this.orderLotUSDSize = Double.parseDouble(orderLotUSDSize);
    }

    @Value("${allPricesCacheSize}")
    public void setAllPricesCacheSize(int allPricesCacheSize) {
        this.allPricesCacheSize = allPricesCacheSize;
    }

    @Value("${bridgeCoin}")
    public void setBridgeCoin(String bridgeCoin) {
        this.bridgeCoin = bridgeCoin;
    }
}
