package org.velikokhatko.stratery1.constants;

public final class Constants {
    public static final String HISTORICAL_DATA_BASE_LINK = "https://data.binance.vision";

    /**
     * Пример: /data/spot/monthly/klines/ADABUSD/1m/ADABUSD-1m-2021-10.zip
     */
    public static final String HISTORICAL_DATA_ADDITIONAL_LINK_TEMPLATE = "/data/spot/monthly/klines/{SYMBOL}/1m/{SYMBOL}-1m-{YEAR}-{MONTH}.zip";
}
