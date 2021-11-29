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
    private double bridgeDepositUSD = 160d;
    private final Map<String, Hold> holdMap = new HashMap<>();

    @Override
    protected double getFreeBridgeCoinUSDBalance() {
        return bridgeDepositUSD;
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
                Optional<Double> currentPrice = getPrice(LocalDateTime.now(), holdSymbol);
                currentPrice.ifPresent(price -> {
                    if (price >= hold.getExpectingPrice()) {
                        bridgeDepositUSD += minusFee(hold.getMoneyAmount() * price);
                        holdMap.remove(holdSymbol);
                        log.info("Закрыта позиция на пару {}: {}\nВсего денег: {}", holdSymbol, hold, countAllMoney());
                    }
                });
            });
        }, 15, TimeUnit.SECONDS);
    }

    @Override
    protected void openLongPosition(RatioParams ratioParams) {
        LocalDateTime currentLDT = LocalDateTime.now();
        Optional<Double> currentPrice = getPrice(currentLDT, ratioParams.getSymbol());
        Optional<Double> oldPrice = getOldPrice(currentLDT, ratioParams);
        if (oldPrice.isPresent() && currentPrice.isPresent() && bridgeDepositUSD > orderLotUSDSize) {
            Double buyingPrice = currentPrice.get();
            double moneyAmountBeforeFee = orderLotUSDSize / buyingPrice;
            Hold hold = new Hold(buyingPrice, oldPrice.get(), minusFee(moneyAmountBeforeFee));
            holdMap.put(ratioParams.getSymbol(), hold);
            bridgeDepositUSD -= moneyAmountBeforeFee * buyingPrice;
            log.info("Открыта позиция на пару {}: {}\nВсего денег: {}", ratioParams.getSymbol(), hold, countAllMoney());
        }
    }

    private Optional<Double> getOldPrice(LocalDateTime currentLDT, RatioParams ratioParams) {
        LocalDateTime oldLDT = currentLDT.minusMinutes(ratioParams.getDeltaMinuteInterval());
        return getPrice(oldLDT, ratioParams.getSymbol());
    }

    private double countAllMoney() {
        double result = bridgeDepositUSD;
        for (Map.Entry<String, Hold> entry : holdMap.entrySet()) {
            Optional<Double> price = getPrice(LocalDateTime.now(), entry.getKey());
            if (price.isPresent()) {
                result += entry.getValue().getMoneyAmount() * price.get();
            }
        }
        return result;
    }

    private Optional<Double> getPrice(LocalDateTime ldt, String symbol) {
        Map<String, Double> symbolPriceMap;
        LocalDateTime ldtTruncated = ldt.truncatedTo(ChronoUnit.MINUTES);
        if ((symbolPriceMap = allPricesCache.get(ldtTruncated)) != null) {
            return Optional.ofNullable(symbolPriceMap.get(symbol));
        }
        if (LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES).equals(ldt)) {
            return Optional.of(Double.parseDouble(binanceApiProvider.getPrice(symbol).getPrice()));
        }
        return Optional.empty();
    }

    @Autowired
    public void setScheduledExecutorService(ScheduledExecutorService scheduledExecutorService) {
        this.scheduledExecutorService = scheduledExecutorService;
    }
}
