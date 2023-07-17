package org.velikokhatko.stratery1.constants;

import java.text.DecimalFormat;
import java.time.Duration;
import java.time.temporal.ChronoUnit;

public final class Constants {
    public static final String HISTORICAL_DATA_BASE_LINK = "https://data.binance.vision";
    /**
     * Пример: /data/spot/daily/klines/ADABUSD/1m/ADABUSD-1m-2021-10-15.zip
     */
    public static final String HISTORICAL_DATA_ADDITIONAL_LINK_TEMPLATE = "/data/spot/daily/klines/{SYMBOL}/1m/{SYMBOL}-1m-{YEAR}-{MONTH}-{DAY}.zip";

    public static final DecimalFormat DOUBLE_FORMAT = new DecimalFormat("#.0#");
    public static final DecimalFormat DOUBLE_VERBOSE_FORMAT = new DecimalFormat("#.0000#");

    public static final Duration DURATION_ONE_DAY = Duration.of(1, ChronoUnit.DAYS);
    public static final Duration DURATION_FIVE_DAYS = Duration.of(5, ChronoUnit.DAYS);

    public static final String CRON_EVERY_MINUTE = "0 * * * * *";
}
