package org.velikokhatko.stratery1.services.trade;

import com.binance.api.client.domain.market.TickerPrice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.util.Assert;
import org.velikokhatko.stratery1.services.api.exchange.ExchangeInfoService;
import org.velikokhatko.stratery1.services.api.provider.AbstractBinanceApiProvider;
import org.velikokhatko.stratery1.services.predictions.PredictionService;
import org.velikokhatko.stratery1.services.ratio.SingleCoinRatioSelectingService;
import org.velikokhatko.stratery1.services.ratio.model.RatioParams;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public abstract class AbstractTradingService {

    protected AbstractBinanceApiProvider binanceApiProvider;
    protected ExchangeInfoService exchangeInfoService;
    protected PredictionService predictionService;
    protected SingleCoinRatioSelectingService ratioSelectingService;
    private double orderLotUSDSize;

    protected Map<LocalDateTime, Map<String, Double>> allPricesCache = new HashMap<>();

    /**
     * ГЛАВНАЯ ФУНКЦИЯ
     * <p>
     * Новая итерация трейдинга каждую минуту
     */
    @Scheduled(cron = "0 * * * * *")
    public void trade() {
        updateAllPricesCache();

        double freeBridgeCoinUSDBalance = getFreeBridgeCoinUSDBalance();
        int availableOrderSlots = (int) (freeBridgeCoinUSDBalance / orderLotUSDSize);
        if (availableOrderSlots > 0) {
            List<String> buyCandidateKeys = exchangeInfoService.getAllSymbolInfoShort().keySet().stream()
                    .filter(predictionService::canBuy)
                    .filter(this::doesNotHolding)
                    .filter(this::isProfitableFall)
                    .collect(Collectors.toList());

        }
    }

    abstract protected boolean doesNotHolding(String s);

    private void updateAllPricesCache() {
        allPricesCache.remove(LocalDateTime.now().minusMinutes(40).truncatedTo(ChronoUnit.MINUTES));
        allPricesCache.put(
                LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES),
                binanceApiProvider.getAllPrices().stream()
                        .collect(Collectors.toMap(
                                TickerPrice::getSymbol,
                                tickerPrice -> Double.valueOf(tickerPrice.getPrice()))
                        )
        );
    }

    protected abstract double getFreeBridgeCoinUSDBalance();

    private boolean isProfitableFall(String symbol) {
        RatioParams ratioParams = ratioSelectingService.selectRatio(symbol);
        LocalDateTime currentTruncatedLDT = LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES);
        LocalDateTime oldTruncatedLDT = currentTruncatedLDT.minusMinutes(ratioParams.getDeltaMinuteInterval());

        Map<String, Double> currentPrices = allPricesCache.get(currentTruncatedLDT);
        Map<String, Double> oldPrices = allPricesCache.get(oldTruncatedLDT);

        if (oldPrices != null && oldPrices.containsKey(symbol)
                && currentPrices != null && currentPrices.containsKey(symbol)) {
            Double oldPrice = oldPrices.get(symbol);
            Double currentPrice = currentPrices.get(symbol);
            return oldPrice > currentPrice
                    && 100d - (currentPrice / oldPrice) * 100 <= ratioParams.getDeltaPercent();
        }

        return false;
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

    @Value("${orderLotUSDSize}")
    public void setOrderLotUSDSize(String orderLotUSDSize) {
        Assert.hasText(orderLotUSDSize, "Не задан orderLotUSDSize");
        this.orderLotUSDSize = Double.parseDouble(orderLotUSDSize);
    }
}
