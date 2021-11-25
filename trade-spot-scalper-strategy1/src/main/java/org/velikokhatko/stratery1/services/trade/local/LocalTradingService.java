package org.velikokhatko.stratery1.services.trade.local;

import org.springframework.stereotype.Service;
import org.velikokhatko.stratery1.services.ratio.model.Hold;
import org.velikokhatko.stratery1.services.trade.AbstractTradingService;

import java.util.HashMap;
import java.util.Map;

@Service
public class LocalTradingService extends AbstractTradingService {
    private double money = 1000d;
    private Map<String, Hold> holdMap = new HashMap<>();

    @Override
    protected double getFreeBridgeCoinUSDBalance() {
        return money;
    }

    @Override
    protected boolean doesNotHolding(String s) {
        return !holdMap.containsKey(s);
    }
}
