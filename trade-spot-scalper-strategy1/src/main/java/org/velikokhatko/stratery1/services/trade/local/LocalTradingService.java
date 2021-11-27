package org.velikokhatko.stratery1.services.trade.local;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.velikokhatko.stratery1.services.ratio.model.Hold;
import org.velikokhatko.stratery1.services.ratio.model.RatioParams;
import org.velikokhatko.stratery1.services.trade.AbstractTradingService;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static org.velikokhatko.stratery1.utils.Utils.minusFee;

@Service
@Slf4j
public class LocalTradingService extends AbstractTradingService {

    private ScheduledExecutorService scheduledExecutorService;
    private double money = 150d;
    private final Map<String, Hold> holdMap = new HashMap<>();

    @Override
    protected double getFreeBridgeCoinUSDBalance() {
        return money;
    }

    @Override
    protected boolean doesNotHolding(String s) {
        return !holdMap.containsKey(s);
    }

    @PostConstruct
    public void closeLongPositions() {
        scheduledExecutorService.schedule(() -> {
            updateAllPricesCache();
            Set<String> holdSymbols = holdMap.keySet();
            holdSymbols.forEach(holdSymbol -> {
                Hold hold = holdMap.get(holdSymbol);
                Optional<Double> currentPrice = getPrice(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES), holdSymbol);
                currentPrice.ifPresent(price -> {
                    if (price >= hold.getExpectingPrice()) {
                        money += minusFee(hold.getMoneyAmount() / hold.getBuyingPrice() * price);
                        holdMap.remove(holdSymbol);
                        log.info("Закрыта позиция на пару {}: {}\nВсего денег: {}", holdSymbol, hold, countAllMoney());
                    }
                });
            });
        }, 15, TimeUnit.SECONDS);
    }

    @Override
    protected void openLongPosition(RatioParams ratioParams) {
        LocalDateTime currentLDT = LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES);
        Optional<Double> currentPrice = getPrice(currentLDT, ratioParams.getSymbol());
        Optional<Double> oldPrice = getOldPrice(currentLDT, ratioParams);
        if (oldPrice.isPresent() && currentPrice.isPresent() && money > orderLotUSDSize) {
            Hold hold = new Hold(currentPrice.get(), oldPrice.get(), minusFee(orderLotUSDSize));
            holdMap.put(ratioParams.getSymbol(), hold);
            money -= orderLotUSDSize;
            log.info("Открыта позиция на пару {}: {}\nВсего денег: {}", ratioParams.getSymbol(), hold, countAllMoney());
        }
    }

    private Optional<Double> getOldPrice(LocalDateTime currentLDT, RatioParams ratioParams) {
        LocalDateTime oldLDT = currentLDT.minusMinutes(ratioParams.getDeltaMinuteInterval());
        return getPrice(oldLDT, ratioParams.getSymbol());
    }

    private double countAllMoney() {
        double result = money;
        for (Map.Entry<String, Hold> entry : holdMap.entrySet()) {
            Optional<Double> price = getPrice(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES), entry.getKey());
            if (price.isPresent()) {
                result += entry.getValue().getMoneyAmount() * price.get();
            }
        }
        return result;
    }

    private Optional<Double> getPrice(LocalDateTime currentLDT, String symbol) {
        Map<String, Double> symbolPriceMap;
        if ((symbolPriceMap = allPricesCache.get(currentLDT)) != null) {
            return Optional.ofNullable(symbolPriceMap.get(symbol));
        } else {
            return Optional.empty();
        }
    }

    @Autowired
    public void setScheduledExecutorService(ScheduledExecutorService scheduledExecutorService) {
        this.scheduledExecutorService = scheduledExecutorService;
    }
}
