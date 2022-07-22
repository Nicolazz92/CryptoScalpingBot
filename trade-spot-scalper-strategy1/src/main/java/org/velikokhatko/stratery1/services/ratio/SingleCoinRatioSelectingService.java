package org.velikokhatko.stratery1.services.ratio;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.velikokhatko.stratery1.exceptions.TraderBotRuntimeException;
import org.velikokhatko.stratery1.services.ratio.model.MarketInterval;
import com.velikokhatko.model.RatioParams;
import org.velikokhatko.stratery1.utils.Utils;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.ExecutorService;

import static org.velikokhatko.stratery1.constants.Constants.DURATION_FIVE_DAYS;
import static org.velikokhatko.stratery1.constants.Constants.DURATION_ONE_DAY;
import static org.velikokhatko.stratery1.services.AppContext.RATIO_CACHE;

@Service
@Slf4j
public class SingleCoinRatioSelectingService {

    private static final double START_MONEY = 100d;
    private SingleCoinRatioReviewService singleCoinRatioReviewService;
    private MarketingIntervalsObtainingService marketingIntervalsObtainingService;
    private RemoteFileExistsCheckingService remoteFileExistsCheckingService;
    private ExecutorService executorService;
    private final Set<String> ratioSelectProcessing = new ConcurrentSkipListSet<>();
    private double ratioSelectingDaysPeriod;
    private int allPricesCacheSize;

    public Optional<RatioParams> selectRatio(String symbol) {
        if (symbol == null) {
            throw new TraderBotRuntimeException("symbol is null");
        }
        if (!ratioSelectProcessing.contains(symbol) && needToUpdateRatioParams(symbol)) {
            //кладем в очередь задачу на заполнение кэша
            executorService.execute(() -> {
                ratioSelectProcessing.add(symbol);
                _selectRatio(symbol).ifPresent(params -> {
                    RATIO_CACHE.put(symbol, params);
                });
                ratioSelectProcessing.remove(symbol);
            });
            return Optional.empty();
        }
        return Optional.ofNullable(RATIO_CACHE.get(symbol));
    }

    private boolean needToUpdateRatioParams(String symbol) {
        return !RATIO_CACHE.containsKey(symbol) || RATIO_CACHE.get(symbol).getFreshLimit().isBefore(LocalDateTime.now());
    }

    private Optional<RatioParams> _selectRatio(String symbol) {
        final List<String> reachableFilesLinks = getReachableFilesLinks(symbol);
        final RatioParams defaultRatioParams = new RatioParams(symbol, 15, 10d,
                LocalDateTime.now().plus(DURATION_ONE_DAY).plusMinutes(RandomUtils.nextLong(0, 60)));

        if (reachableFilesLinks.isEmpty()) {
            log.warn("При подборе коэффициентов для пары {} не нашлось доступных ссылок на исторические данные", symbol);
            log.warn("Для пары {} были выбраны дефолтные коэффициенты {}", symbol, defaultRatioParams);
            return Optional.of(defaultRatioParams);
        }

        Duration freshDuration = reachableFilesLinks.size() > ratioSelectingDaysPeriod / 100 * 90
                ? DURATION_FIVE_DAYS.plusMinutes(RandomUtils.nextLong(0, 60))
                : DURATION_ONE_DAY.plusMinutes(RandomUtils.nextLong(0, 60));

        Map<LocalDateTime, MarketInterval> marketIntervalMap = marketingIntervalsObtainingService
                .obtainCsvIntervals(reachableFilesLinks);
        if (marketIntervalMap.isEmpty()) {
            log.warn("При подборе коэффициентов для пары {} после парсинга исторических данных коллекция оказалась пустой", symbol);
            log.warn("Для пары {} были выбраны дефолтные коэффициенты {}", symbol, defaultRatioParams);
            return Optional.of(defaultRatioParams);
        }

        final RatioParams result = review(symbol, marketIntervalMap, freshDuration);
        log.info("При подборе коэффициентов для пары {} были выбраны коэффициенты {}", symbol, result);
        return Optional.ofNullable(result);
    }

    private List<String> getReachableFilesLinks(String symbol) {
        List<String> result = new ArrayList<>();
        for (int minusDays = 1; minusDays < ratioSelectingDaysPeriod; minusDays++) {
            final String url = Utils.getKlinesZipURLBySymbol(symbol, minusDays);
            if (remoteFileExistsCheckingService.isFileExists(url)) {
                result.add(url);
            }
        }

        return result;
    }

    private RatioParams review(String symbol, Map<LocalDateTime, MarketInterval> marketIntervalMap, Duration freshDuration) {
        List<RatioParams> paramsReviews = new ArrayList<>();
        for (int minuteInterval = 3; minuteInterval <= allPricesCacheSize; minuteInterval++) {
            for (double deltaPercent = 3; deltaPercent <= 20; deltaPercent++) {
                final LocalDateTime freshLimit = LocalDateTime.now().plus(freshDuration);
                final RatioParams paramsReview = new RatioParams(symbol, minuteInterval, deltaPercent, freshLimit);
                final Double resultMoney = singleCoinRatioReviewService.process(marketIntervalMap, paramsReview);
                paramsReview.setResultPercent(resultMoney / START_MONEY * 100);
                paramsReviews.add(paramsReview);
            }
        }

        final RatioParams maxPercentRP = paramsReviews.stream()
                .max(Comparator.comparing(RatioParams::getResultPercent)).orElse(null);
        if (maxPercentRP == null) {
            return null;
        }
        final RatioParams minDeltaMinuteInterval = paramsReviews.stream()
                .filter(pr -> maxPercentRP.getResultPercent().equals(pr.getResultPercent()))
                .min(Comparator.comparing(RatioParams::getDeltaMinuteInterval)).orElse(null);
        if (minDeltaMinuteInterval == null) {
            return null;
        }
        return paramsReviews.stream()
                .filter(pr -> maxPercentRP.getResultPercent().equals(pr.getResultPercent()))
                .filter(pr -> minDeltaMinuteInterval.getDeltaMinuteInterval().equals(pr.getDeltaMinuteInterval()))
                .min(Comparator.comparing(RatioParams::getDeltaPercent)).orElse(null);
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
    public void setExecutorServiceFixedSize(ExecutorService executorService) {
        this.executorService = executorService;
    }

    @Value("${ratioSelectingDaysPeriod}")
    public void setRatioSelectingDaysPeriod(double ratioSelectingDaysPeriod) {
        this.ratioSelectingDaysPeriod = ratioSelectingDaysPeriod;
    }

    @Value("${allPricesCacheSize}")
    public void setAllPricesCacheSize(int allPricesCacheSize) {
        this.allPricesCacheSize = allPricesCacheSize;
    }
}
