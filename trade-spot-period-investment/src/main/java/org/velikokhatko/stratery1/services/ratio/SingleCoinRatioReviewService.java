package org.velikokhatko.stratery1.services.ratio;

import org.springframework.stereotype.Service;
import org.velikokhatko.stratery1.services.ratio.model.Hold;
import org.velikokhatko.stratery1.services.ratio.model.MarketInterval;
import org.velikokhatko.stratery1.services.ratio.model.RatioParams;

import java.time.LocalDateTime;
import java.util.Iterator;
import java.util.Map;

import static org.velikokhatko.stratery1.utils.Utils.minusFee;

/**
 * Сервис оценивает конкретный набор ratioParams, сколько при идеальных условиях можно было бы получить дохода
 * на последовательности цен marketIntervals.
 */
@Service
public class SingleCoinRatioReviewService {

    private static final double START_MONEY = 100d;

    public Double process(Map<LocalDateTime, MarketInterval> marketIntervals,
                          RatioParams ratioParams) {
        double resultMoney = START_MONEY;
        Hold hold = null;

        Iterator<LocalDateTime> iterator = marketIntervals.keySet().stream().sorted().iterator();

        for (int i = 0; i < ratioParams.getDeltaMinuteInterval() + 1 && iterator.hasNext(); i++) {
            iterator.next();
        }

        while (iterator.hasNext()) {
            final LocalDateTime currentLDT = iterator.next();
            double currentPrice = marketIntervals.get(currentLDT).getOpen();

            if (profitableBuy(ratioParams, hold, currentLDT, marketIntervals)) {
                Double oldPrice = getOldPrice(marketIntervals, ratioParams, currentLDT);
                if (oldPrice != null) {
                    hold = new Hold(currentPrice, oldPrice, minusFee(resultMoney));
                }
            } else if (hold != null && currentPrice >= hold.getExpectingPrice()) {
                hold.setSellingDate(currentLDT);
                resultMoney = minusFee(hold.getMoneyAmount() / hold.getBuyingPrice() * currentPrice);
                ratioParams.setDealsCount(ratioParams.getDealsCount() + 1);
                hold = null;
            }
        }

        ratioParams.setResultPercent(resultMoney / START_MONEY * 100);

        return resultMoney;
    }

    private boolean profitableBuy(RatioParams ratioParams,
                                  Hold hold,
                                  LocalDateTime currentLDT,
                                  Map<LocalDateTime, MarketInterval> marketIntervals) {
        if (hold != null) {
            return false;
        }
        Double oldPrice = getOldPrice(marketIntervals, ratioParams, currentLDT);
        if (oldPrice == null) {
            return false;
        }
        return isPriceFallingDeepEnough(ratioParams, marketIntervals.get(currentLDT).getOpen(), oldPrice);
    }

    private Double getOldPrice(Map<LocalDateTime, MarketInterval> marketIntervals, RatioParams ratioParams, LocalDateTime currentLDT) {
        LocalDateTime oldLDT = currentLDT.minusMinutes(ratioParams.getDeltaMinuteInterval());
        final MarketInterval marketInterval = marketIntervals.get(oldLDT);
        return marketInterval != null ? marketInterval.getOpen() : null;
    }

    private boolean isPriceFallingDeepEnough(RatioParams ratioParams, double currentPrice, double oldPrice) {
        return oldPrice > currentPrice
                && 100d - (currentPrice / oldPrice) * 100 >= ratioParams.getDeltaPercent();
    }
}
