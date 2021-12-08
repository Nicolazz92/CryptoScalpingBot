package org.velikokhatko.stratery1.services.trade.local;

import com.binance.api.client.domain.market.TickerPrice;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.velikokhatko.stratery1.services.ratio.model.Hold;
import org.velikokhatko.stratery1.services.ratio.model.RatioParams;
import org.velikokhatko.stratery1.services.trade.AbstractTradingService;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import static org.velikokhatko.stratery1.utils.Utils.minusFee;
import static org.velikokhatko.stratery1.utils.Utils.truncate;

@Service
@Slf4j
public class LocalTradingService extends AbstractTradingService {

    private double bridgeDepositUSD = 500d;
    private final Map<String, Hold> holdMap = new ConcurrentHashMap<>();
    private final Map<LocalDateTime, Map<String, Double>> allPricesLocalCache = new ConcurrentHashMap<>();

    @Override
    protected double getFreeBridgeCoinUSDBalance() {
        return bridgeDepositUSD;
    }

    @Override
    protected boolean doesNotHolding(String s) {
        return !holdMap.containsKey(s);
    }

    @Scheduled(fixedDelay = 30000)
    public void closeLongPositions() {
        updateAllPricesCache(allPricesLocalCache);

        Set<String> holdSymbols = holdMap.keySet();
        for (String holdSymbol : holdSymbols) {
            Hold hold = holdMap.get(holdSymbol);
            Optional<Double> currentPrice = getPrice(0, holdSymbol);
            currentPrice.ifPresent(price -> {
                if (price >= hold.getExpectingPrice()) {
                    bridgeDepositUSD += minusFee(hold.getMoneyAmount() * price);
                    holdMap.remove(holdSymbol);
                    log.info("Закрыта позиция на пару {}: {}\nВсего денег: {}$", holdSymbol, hold, countAllMoney());
                }
            });
        }
    }

    @Override
    protected void openLongPosition(RatioParams ratioParams) {
        Optional<Double> currentPrice = getPrice(0, ratioParams.getSymbol());
        Optional<Double> oldPrice = getOldPrice(ratioParams);
        if (oldPrice.isPresent() && currentPrice.isPresent() && bridgeDepositUSD > orderLotUSDSize) {
            Double buyingPrice = currentPrice.get();
            double moneyAmountBeforeFee = orderLotUSDSize / buyingPrice;
            Hold hold = new Hold(buyingPrice, oldPrice.get(), minusFee(moneyAmountBeforeFee));
            holdMap.put(ratioParams.getSymbol(), hold);
            bridgeDepositUSD -= moneyAmountBeforeFee * buyingPrice;
            log.info("Открыта позиция на пару {}: {}\nВсего денег: {}$", ratioParams.getSymbol(), hold, countAllMoney());
        }
    }

    private Optional<Double> getOldPrice(RatioParams ratioParams) {
        Integer minusMinutes = ratioParams.getDeltaMinuteInterval();
        Optional<Double> price = getPrice(minusMinutes, ratioParams.getSymbol());
        return price;
    }

    public double countAllMoney() {
        double result = bridgeDepositUSD;
        for (Map.Entry<String, Hold> entry : holdMap.entrySet()) {
            Optional<Double> price = getPrice(0, entry.getKey());
            if (price.isPresent()) {
                result += entry.getValue().getMoneyAmount() * price.get();
            }
        }
        return result;
    }

    private Optional<Double> getPrice(Integer minusMinutes, String symbol) {
        Map<String, Double> symbolPriceMap;
        LocalDateTime key = truncate(LocalDateTime.now().minusMinutes(minusMinutes));
        if ((symbolPriceMap = allPricesLocalCache.get(key)) != null && symbolPriceMap.get(symbol) != null) {
            return Optional.ofNullable(symbolPriceMap.get(symbol));
        }
        if (minusMinutes == 0) {
            Optional<TickerPrice> priceOptional = binanceApiProvider.getPrice(symbol);
            if (priceOptional.isPresent()) {
                return Optional.of(Double.parseDouble(priceOptional.get().getPrice()));
            }
        }
        return Optional.empty();
    }
}
