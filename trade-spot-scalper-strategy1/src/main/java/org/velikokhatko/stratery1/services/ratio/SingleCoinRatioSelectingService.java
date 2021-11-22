package org.velikokhatko.stratery1.services.ratio;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.velikokhatko.stratery1.services.api.exchange.SymbolInfoShort;
import org.velikokhatko.stratery1.services.ratio.model.MarketInterval;
import org.velikokhatko.stratery1.services.ratio.model.RatioParams;
import org.velikokhatko.stratery1.utils.Utils;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class SingleCoinRatioSelectingService {

    private static final double START_MONEY = 100d;
    private static final Duration DURATION_ONE_DAY = Duration.of(1, ChronoUnit.DAYS);
    private static final Duration DURATION_FIVE_DAY = Duration.of(5, ChronoUnit.DAYS);
    private SingleCoinRatioReviewService singleCoinRatioReviewService;
    private MarketingIntervalsObtainingService marketingIntervalsObtainingService;
    private RemoteFileExistsCheckingService remoteFileExistsCheckingService;
    private double ratioSelectingPeriod;

    public RatioParams selectRatio(SymbolInfoShort symbolInfo) {
        final List<String> reachableFilesLinks = getReachableFilesLinks(symbolInfo);

        if (reachableFilesLinks.isEmpty()) {
            return new RatioParams(15, 10d, LocalDateTime.now().plus(DURATION_ONE_DAY));
        }

        Duration freshDuration = reachableFilesLinks.size() > ratioSelectingPeriod / 100 * 90
                ? DURATION_FIVE_DAY
                : DURATION_ONE_DAY;

        Map<LocalDateTime, MarketInterval> marketIntervalMap = marketingIntervalsObtainingService
                .obtainCsvIntervals(reachableFilesLinks);
        final RatioParams result = review(marketIntervalMap, freshDuration);
        log.info("Для пары {} были выбраны коэффициенты {}", symbolInfo.getSymbol(), result);
        return result;
    }

    private List<String> getReachableFilesLinks(SymbolInfoShort symbolInfo) {
        List<String> result = new ArrayList<>();
        for (int minusDays = 1; minusDays < ratioSelectingPeriod; minusDays++) {
            final String url = Utils.getKlinesZipURLBySymbol(symbolInfo.getSymbol(), minusDays);
            if (remoteFileExistsCheckingService.isFileExists(url)) {
                result.add(url);
            }
        }

        return result;
    }

    private RatioParams review(Map<LocalDateTime, MarketInterval> marketIntervalMap, Duration freshDuration) {
        List<RatioParams> paramsReviews = new ArrayList<>();
        for (int minuteInterval = 5; minuteInterval <= 25; minuteInterval++) {
            for (double deltaPercent = 3; deltaPercent <= 20; deltaPercent++) {
                final LocalDateTime freshLimit = LocalDateTime.now().plus(freshDuration);
                final RatioParams paramsReview = new RatioParams(minuteInterval, deltaPercent, freshLimit);
                final Double resultMoney = singleCoinRatioReviewService.process(marketIntervalMap, paramsReview);
                paramsReview.setResultPercent(resultMoney / START_MONEY * 100);
                paramsReviews.add(paramsReview);
            }
        }

        final RatioParams maxPercentRP = paramsReviews.stream()
                .max(Comparator.comparing(RatioParams::getResultPercent)).get();
        final RatioParams minDeltaMinuteInterval = paramsReviews.stream()
                .filter(pr -> maxPercentRP.getResultPercent().equals(pr.getResultPercent()))
                .min(Comparator.comparing(RatioParams::getDeltaMinuteInterval)).get();
        final RatioParams result = paramsReviews.stream()
                .filter(pr -> maxPercentRP.getResultPercent().equals(pr.getResultPercent()))
                .filter(pr -> minDeltaMinuteInterval.getDeltaMinuteInterval().equals(pr.getDeltaMinuteInterval()))
                .min(Comparator.comparing(RatioParams::getDeltaPercent)).get();

//        paramsReviews.stream().sorted(Comparator.comparing(RatioParams::getResultPercent)).forEach(System.out::println);
//        System.out.println("\nОсновной результат: " + result);
        return result;
    }

    @Autowired
    public void setSingleCoinRatioReviewService(SingleCoinRatioReviewService singleCoinRatioReviewService) {
        this.singleCoinRatioReviewService = singleCoinRatioReviewService;
    }

    @Autowired
    public void setMarketingIntervalsObtainingService(MarketingIntervalsObtainingService marketingIntervalsObtainingService) {
        this.marketingIntervalsObtainingService = marketingIntervalsObtainingService;
    }

    @Autowired
    public void setRemoteFileExistsCheckingService(RemoteFileExistsCheckingService remoteFileExistsCheckingService) {
        this.remoteFileExistsCheckingService = remoteFileExistsCheckingService;
    }

    @Value("${ratioSelectingPeriod}")
    public void setRatioSelectingPeriod(double ratioSelectingPeriod) {
        this.ratioSelectingPeriod = ratioSelectingPeriod;
    }
}
