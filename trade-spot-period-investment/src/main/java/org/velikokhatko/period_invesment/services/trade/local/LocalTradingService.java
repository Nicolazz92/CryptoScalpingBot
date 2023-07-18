package org.velikokhatko.period_invesment.services.trade.local;

import com.binance.api.client.domain.market.TickerPrice;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.velikokhatko.period_invesment.services.model.Hold;
import org.velikokhatko.period_invesment.services.trade.AbstractTradingService;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Service
@Slf4j
public class LocalTradingService extends AbstractTradingService {

    private double bridgeDepositUSD = 500d;
    private final Map<String, Hold> holdMap = new ConcurrentHashMap<>();

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
    }

    public double countAllMoney() {
        double result = bridgeDepositUSD;
        for (Map.Entry<String, Hold> entry : holdMap.entrySet()) {
            Optional<Double> price = getPrice("symbol");
            if (price.isPresent()) {
                result += entry.getValue().getMoneyAmount() * price.get();
            }
        }
        return result;
    }

    private Optional<Double> getPrice(String symbol) {
        Optional<TickerPrice> priceOptional = binanceApiProvider.getPrice(symbol);
        return priceOptional.map(tickerPrice -> Double.parseDouble(tickerPrice.getPrice()));
    }
}
