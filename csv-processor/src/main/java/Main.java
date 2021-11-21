import model.MarketInterval;
import model.ParamsReview;
import service.CSVProcessingService;
import service.ReviewProcessingService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

public class Main {
    private static final CSVProcessingService csvProcessingService = new CSVProcessingService();
    private static final ReviewProcessingService reviewProcessingService = new ReviewProcessingService();
    private static final double START_MONEY = 100d;

    public static void main(String[] args) {
        final String fileName = "FTTBNB-1m-2021-06.csv";
        final Map<LocalDateTime, MarketInterval> marketIntervals = csvProcessingService.parseCsvIntervals(fileName);

        final LocalDateTime endReviewLDT = marketIntervals.keySet().stream().max(LocalDateTime::compareTo).get();
        review(marketIntervals, endReviewLDT);
    }

    private static void review(Map<LocalDateTime, MarketInterval> marketIntervals, LocalDateTime endReviewLDT) {
        List<ParamsReview> paramsReviews = new ArrayList<>();
        for (int minuteInterval = 5; minuteInterval <= 25; minuteInterval++) {
            for (double deltaPercent = 3; deltaPercent <= 20; deltaPercent++) {
                final ParamsReview paramsReview = new ParamsReview(endReviewLDT, 25, minuteInterval, deltaPercent);
                final Double resultMoney = reviewProcessingService.process(START_MONEY, marketIntervals, paramsReview);
                paramsReview.setResultPercent(resultMoney / START_MONEY * 100);
                paramsReviews.add(paramsReview);
            }
        }

        paramsReviews.sort(Comparator.comparing(ParamsReview::getResultPercent));
        System.out.println(paramsReviews.size());
        paramsReviews.forEach(System.out::println);
    }
}
