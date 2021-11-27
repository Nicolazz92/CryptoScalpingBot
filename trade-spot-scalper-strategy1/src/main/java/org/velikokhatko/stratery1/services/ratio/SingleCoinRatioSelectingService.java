package org.velikokhatko.stratery1.services.ratio;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.velikokhatko.stratery1.services.ratio.model.MarketInterval;
import org.velikokhatko.stratery1.services.ratio.model.RatioParams;
import org.velikokhatko.stratery1.utils.Utils;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;

import static org.velikokhatko.stratery1.constants.Constants.DURATION_FIVE_DAYS;
import static org.velikokhatko.stratery1.constants.Constants.DURATION_ONE_DAY;

@Service
@Slf4j
public class SingleCoinRatioSelectingService {

    private static final double START_MONEY = 100d;
    private SingleCoinRatioReviewService singleCoinRatioReviewService;
    private MarketingIntervalsObtainingService marketingIntervalsObtainingService;
    private RemoteFileExistsCheckingService remoteFileExistsCheckingService;
    private ExecutorService executorServiceFixedSize;
    private final Map<String, RatioParams> cache = new ConcurrentHashMap<>();
    private double ratioSelectingPeriod;

    public Optional<RatioParams> selectRatio(String symbol) {
        if (!cache.containsKey(symbol) || cache.get(symbol).getFreshLimit().isBefore(LocalDateTime.now())) {
            //кладем в очередь задачу на заполнение кэша
            executorServiceFixedSize.execute(() -> {
                if (cache.containsKey(symbol)) {
                    return;
                }
                RatioParams ratioParams = _selectRatio(symbol);
                cache.put(symbol, ratioParams);
            });
            return Optional.empty();
        }
        return Optional.ofNullable(cache.get(symbol));
    }

    private RatioParams _selectRatio(String symbol) {
        final List<String> reachableFilesLinks = getReachableFilesLinks(symbol);
        final RatioParams defaultRatioParams = new RatioParams(symbol, 15, 10d,
                LocalDateTime.now().plus(DURATION_ONE_DAY).plusMinutes(RandomUtils.nextLong(0, 240)));

        if (reachableFilesLinks.isEmpty()) {
            log.warn("При подборе коэффициентов для пары {} не нашлось доступных ссылок на исторические данные", symbol);
            log.warn("Для пары {} были выбраны дефолтные коэффициенты {}", symbol, defaultRatioParams);
            return defaultRatioParams;
        }

        Duration freshDuration = reachableFilesLinks.size() > ratioSelectingPeriod / 100 * 90
                ? DURATION_FIVE_DAYS.plusMinutes(RandomUtils.nextLong(0, 240))
                : DURATION_ONE_DAY.plusMinutes(RandomUtils.nextLong(0, 240));

        Map<LocalDateTime, MarketInterval> marketIntervalMap = marketingIntervalsObtainingService
                .obtainCsvIntervals(reachableFilesLinks);
        if (marketIntervalMap.isEmpty()) {
            log.warn("При подборе коэффициентов для пары {} после парсинга исторических данных коллекция оказалась пустой", symbol);
            log.warn("Для пары {} были выбраны дефолтные коэффициенты {}", symbol, defaultRatioParams);
            return defaultRatioParams;
        }

        final RatioParams result = review(symbol, marketIntervalMap, freshDuration);
        log.info("При подборе коэффициентов для пары {} были выбраны коэффициенты {}", symbol, result);
        return result;
    }

    private List<String> getReachableFilesLinks(String symbol) {
        List<String> result = new ArrayList<>();
        for (int minusDays = 1; minusDays < ratioSelectingPeriod; minusDays++) {
            final String url = Utils.getKlinesZipURLBySymbol(symbol, minusDays);
            if (remoteFileExistsCheckingService.isFileExists(url)) {
                result.add(url);
            }
        }

        return result;
    }

    private RatioParams review(String symbol, Map<LocalDateTime, MarketInterval> marketIntervalMap, Duration freshDuration) {
        List<RatioParams> paramsReviews = new ArrayList<>();
        for (int minuteInterval = 5; minuteInterval <= 30; minuteInterval++) {
            for (double deltaPercent = 0; deltaPercent <= 20; deltaPercent++) {
                final LocalDateTime freshLimit = LocalDateTime.now().plus(freshDuration);
                final RatioParams paramsReview = new RatioParams(symbol, minuteInterval, deltaPercent, freshLimit);
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

    @Autowired
    public void setExecutorServiceFixedSize(ExecutorService executorServiceFixedSize) {
        this.executorServiceFixedSize = executorServiceFixedSize;
    }

    @Value("${ratioSelectingPeriod}")
    public void setRatioSelectingPeriod(double ratioSelectingPeriod) {
        this.ratioSelectingPeriod = ratioSelectingPeriod;
    }
}
