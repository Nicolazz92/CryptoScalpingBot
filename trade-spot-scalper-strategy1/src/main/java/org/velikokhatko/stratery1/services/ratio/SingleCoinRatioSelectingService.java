package org.velikokhatko.stratery1.services.ratio;

import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.velikokhatko.stratery1.services.api.exchange.SymbolInfoShort;
import org.velikokhatko.stratery1.services.ratio.model.MarketInterval;
import org.velikokhatko.stratery1.services.ratio.model.RatioParams;
import org.velikokhatko.stratery1.utils.Utils;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Service
public class SingleCoinRatioSelectingService {

    private static final double START_MONEY = 100d;
    private SingleCoinRatioReviewService singleCoinRatioReviewService;
    private MarketingIntervalsObtainingService marketingIntervalsObtainingService;
    private RemoteFileExistsCheckingService remoteFileExistsCheckingService;

    public RatioParams selectRatio(SymbolInfoShort symbolInfo) {
        final List<String> reachableFilesLinks = getReachableFilesLinks(symbolInfo);

        //Устанавливаем срок свежести будущих соотношений.
        //Если имеем 3 последовательных ссылки на месяцы, данные будут свежими до начала следующего месяца,
        //пока на сервер(предположительно) не выложат данные за следующий месяц.
        Duration freshDuration = reachableFilesLinks.size() == 3
                ? Duration.of(ChronoUnit.DAYS.between(LocalDate.now(), LocalDate.now().withDayOfMonth(1).plusMonths(1)), ChronoUnit.DAYS)
                //Иначе данные следует обновить через день
                : Duration.of(1, ChronoUnit.DAYS);

        Map<LocalDateTime, MarketInterval> marketIntervalMap = marketingIntervalsObtainingService
                .obtainCsvIntervals(new ArrayList<>());
        review(marketIntervalMap);
        return null;
    }

    private List<String> getReachableFilesLinks(SymbolInfoShort symbolInfo) {
        List<String> result = new ArrayList<>();
        Map<Integer, String> urlLinkMap = new HashMap<>();
        for (int minusMonth = 1; minusMonth < 4; minusMonth++) {
            final String url = Utils.getKlinesZipURLBySymbol(symbolInfo.symbol, minusMonth);
            if (remoteFileExistsCheckingService.isFileExists(url)) {
                urlLinkMap.put(minusMonth, url);
            }
        }

        //проверяем доступность ближайшего месяца, если его ещё нет на сервере, начинаем с месяца до этого
        Pair<Integer, Integer> pastMonthRange = urlLinkMap.containsKey(1)
                ? Pair.of(1, 3)
                : Pair.of(2, 4);

        // добавляем последовательные месяцы, от ближайшего к дальнему. Если последовательность прерывается, останавливаем добавление
        for (int minusMonth = pastMonthRange.getLeft();
             minusMonth <= pastMonthRange.getRight() && urlLinkMap.containsKey(minusMonth);
             minusMonth++) {
            result.add(urlLinkMap.get(minusMonth));
        }

        return result;
    }

    private void review(Map<LocalDateTime, MarketInterval> marketIntervalMap, LocalDateTime freshLimit) {
        List<RatioParams> paramsReviews = new ArrayList<>();
        for (int minuteInterval = 5; minuteInterval <= 25; minuteInterval++) {
            for (double deltaPercent = 3; deltaPercent <= 20; deltaPercent++) {
                final RatioParams paramsReview = new RatioParams(minuteInterval, deltaPercent, freshLimit);
                final Double resultMoney = singleCoinRatioReviewService.process(marketIntervalMap, paramsReview);
                paramsReview.setResultPercent(resultMoney / START_MONEY * 100);
                paramsReviews.add(paramsReview);
            }
        }

        Comparator<RatioParams> ratioParamsComparator = Comparator
                .comparing(RatioParams::getResultPercent).reversed()
                .thenComparing(RatioParams::getDeltaPercent).reversed();
        paramsReviews.sort(ratioParamsComparator);
        System.out.println(paramsReviews.size());
        System.out.println("Основной результат: " + paramsReviews.get(0));
        paramsReviews.forEach(System.out::println);
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
    public void setUrlReachableCheckingService(RemoteFileExistsCheckingService remoteFileExistsCheckingService) {
        this.remoteFileExistsCheckingService = remoteFileExistsCheckingService;
    }
}
