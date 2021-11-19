package service;

import model.Hold;
import model.MarketInterval;
import model.ParamsReview;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Map;

public class ReviewProcessingService {

    public Double process(Double startMoney,
                          Map<LocalDateTime, MarketInterval> marketIntervals,
                          ParamsReview paramsReview) {
        double resultMoney = startMoney;
        Hold hold = null;

        LocalDateTime startLDT = paramsReview.getEndReviewLDT().minusDays(paramsReview.getFullDayReviewInterval());
        LocalDateTime currentLDT = startLDT.truncatedTo(ChronoUnit.MINUTES).plusMinutes(paramsReview.getDeltaMinuteInterval());

        while (currentLDT.isBefore(paramsReview.getEndReviewLDT())) {

            double currentPrice = marketIntervals.get(currentLDT).getOpen();

            if (profitableBuy(paramsReview, hold, currentLDT, marketIntervals)) {
                double oldPrice = getOldPrice(marketIntervals, paramsReview, currentLDT);
                hold = new Hold(currentPrice, oldPrice, minusFee(resultMoney));
            } else if (hold != null && currentPrice >= hold.getExpectingPrice()) {
                hold.setSellingDate(currentLDT);
//                System.out.println(hold);
                resultMoney = minusFee(hold.getMoneyAmount() / hold.getBuyingPrice() * currentPrice);
                paramsReview.setDealsCount(paramsReview.getDealsCount() + 1);
                hold = null;
            }

            currentLDT = currentLDT.plusMinutes(1);
        }

        return resultMoney;
    }

    /**
     * 0.1% - самый большой процент
     *
     * @param resultMoney до уплаты комиссии
     * @return после уплаты комиссии
     */
    private double minusFee(double resultMoney) {
        return resultMoney / 1000 * 999;
    }

    private boolean profitableBuy(ParamsReview paramsReview,
                                  Hold hold,
                                  LocalDateTime currentLDT,
                                  Map<LocalDateTime, MarketInterval> marketIntervals) {
        if (hold != null) {
            return false;
        }
        double oldPrice = getOldPrice(marketIntervals, paramsReview, currentLDT);
        return isPriceFallingDeepEnough(paramsReview, marketIntervals.get(currentLDT).getOpen(), oldPrice);
    }

    private double getOldPrice(Map<LocalDateTime, MarketInterval> marketIntervals, ParamsReview paramsReview, LocalDateTime currentLDT) {
        LocalDateTime oldLDT = currentLDT.minusMinutes(paramsReview.getDeltaMinuteInterval());
        return marketIntervals.get(oldLDT).getOpen();
    }

    private boolean isPriceFallingDeepEnough(ParamsReview paramsReview, double currentPrice, double oldPrice) {
        return oldPrice > currentPrice
                && 100d - (currentPrice / oldPrice) * 100 <= paramsReview.getDeltaPercent();
    }
}
