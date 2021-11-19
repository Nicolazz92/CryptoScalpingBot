package org.velikokhatko.stratery1.services.ratio;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.util.Assert;
import org.velikokhatko.stratery1.services.ratio.model.MarketInterval;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Map;

@SpringBootTest
//@ActiveProfiles("production")
@ActiveProfiles("testnet")
class CSVProcessingServiceTest {

    @Autowired
    private CSVProcessingService csvProcessingService;

    @Test
    public void parseCsvIntervalsTest() {
        final Map<LocalDateTime, MarketInterval> intervalMap = csvProcessingService.parseCsvIntervals(Arrays.asList(
                "https://data.binance.vision/data/spot/monthly/klines/ADABUSD/1m/ADABUSD-1m-2021-08.zip",
                "https://data.binance.vision/data/spot/monthly/klines/ADABUSD/1m/ADABUSD-1m-2021-09.zip",
                "https://data.binance.vision/data/spot/monthly/klines/ADABUSD/1m/ADABUSD-1m-2021-10.zip"
        ));
        System.out.println(intervalMap.size());
        Assert.isTrue(132090 == intervalMap.size(), "Что-то не так со сбором исторических данных");
    }
}