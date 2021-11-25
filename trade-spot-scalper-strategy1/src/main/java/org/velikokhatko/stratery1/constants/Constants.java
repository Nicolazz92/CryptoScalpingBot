package org.velikokhatko.stratery1.constants;

import java.text.DecimalFormat;

public final class Constants {
    public static final String HISTORICAL_DATA_BASE_LINK = "https://data.binance.vision";

    /**
     * Пример: /data/spot/daily/klines/ADABUSD/1m/ADABUSD-1m-2021-10-15.zip
     */
    public static final String HISTORICAL_DATA_ADDITIONAL_LINK_TEMPLATE = "/data/spot/daily/klines/{SYMBOL}/1m/{SYMBOL}-1m-{YEAR}-{MONTH}-{DAY}.zip";

    public static final DecimalFormat DOUBLE_FORMAT = new DecimalFormat("#.0#");
    public static final DecimalFormat DOUBLE_VERBOSE_FORMAT = new DecimalFormat("#.0000#");
}
