package com.binance.bot.investing_com.prediction;

import com.binance.api.client.domain.market.OrderBookEntry;
import com.binance.bot.binance.services.DataProvider;
import org.apache.commons.lang3.StringUtils;

import java.time.LocalDateTime;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static com.binance.bot.binance.Coins.XRP_USD;
import static com.binance.bot.investing_com.prediction.PredictionPeriods.FIVE_MINUTES;

public class PredictionTest {

    public static void main(String[] args) {
        test();
    }

    public static void test() {
        Map<Boolean, Integer> results = new HashMap<>();
        results.put(Boolean.TRUE, 0);
        results.put(Boolean.FALSE, 0);

        while (true) {
            try {
                DataProvider dataProvider = new DataProvider();
                Optional<OrderBookEntry> oldPriceOptional = dataProvider.getPrice(false, XRP_USD);

                PredictionProviderService predictionProviderService = new PredictionProviderService();
                Optional<EnumMap<PredictionPeriods, PredictionCases>> predictions = predictionProviderService.getPredictions(XRP_USD);

                if (oldPriceOptional.isPresent() && StringUtils.isNotBlank(oldPriceOptional.get().getPrice())
                        && predictions.isPresent()) {
                    double oldPrice = Double.parseDouble(oldPriceOptional.get().getPrice());
                    PredictionPeriods predictionPeriod = FIVE_MINUTES;
                    PredictionCases predictionCase = predictions.get().get(predictionPeriod);

                    System.out.println(predictionPeriod.getLabel() + " sleeping...");
                    Thread.sleep(predictionPeriod.getMilliseconds());

                    Optional<OrderBookEntry> newPriceOptional = dataProvider.getPrice(false, XRP_USD);
                    if (newPriceOptional.isPresent() && StringUtils.isNotBlank(newPriceOptional.get().getPrice())) {
                        double newPrice = Double.parseDouble(newPriceOptional.get().getPrice());

                        switch (predictionCase) {
                            case STRONG_SELL, SELL -> {
                                if (oldPrice > newPrice) {
                                    put(results, Boolean.TRUE);
                                } else {
                                    put(results, Boolean.FALSE);
                                }
                            }
//                            case NEUTRAL -> {
//                                if (oldPrice == newPrice) {
//                                    put(results, Boolean.TRUE);
//                                } else {
//                                    put(results, Boolean.FALSE);
//                                }
//                            }
                            case STRONG_BUY, BUY -> {
                                if (oldPrice < newPrice) {
                                    put(results, Boolean.TRUE);
                                } else {
                                    put(results, Boolean.FALSE);
                                }
                            }
                        }

                        System.out.println(LocalDateTime.now());
                        results.forEach((key, value) -> System.out.println("" + key + " - " + value + "; "));
                    }
                }
                System.out.println("30 seconds sleeping...");
                Thread.sleep(1000 * 30);
            } catch (RuntimeException e) {
                System.out.println(e.getMessage());
            } catch (InterruptedException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private static Integer put(Map<Boolean, Integer> results, Boolean aTrue) {
        return results.put(aTrue, results.get(aTrue) + 1);
    }
}
