package org.velikokhatko.period_invesment.services.trade;

import com.binance.api.client.domain.market.TickerPrice;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.velikokhatko.period_invesment.services.api.exchange.ExchangeInfoService;
import org.velikokhatko.period_invesment.services.api.provider.AbstractBinanceApiProvider;
import org.velikokhatko.period_invesment.services.predictions.PredictionService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.stream.Collectors;

import static org.velikokhatko.period_invesment.utils.Utils.truncate;

@Slf4j
public abstract class AbstractTradingService {

    protected AbstractBinanceApiProvider binanceApiProvider;
    protected ExchangeInfoService exchangeInfoService;
    protected PredictionService predictionService;
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
