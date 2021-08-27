package com.binance.bot.investing_com.prediction;

import lombok.Getter;

@Getter
public enum PredictionPeriods {
    FIVE_MINUTES("5 minutes", 5 * 60 * 1000),
    FIFTEEN_MINUTES("15 minutes", 15 * 60 * 1000),
    HOURLY("1 hour", 60 * 60 * 1000),
    DAILY("1 day", 1440 * 60 * 1000),
    MONTHLY("1 month", ((long) 43800) * 60 * 1000);

    private final long milliseconds;
    private final String label;

    PredictionPeriods(String label, long milliseconds) {
        this.label = label;
        this.milliseconds = milliseconds;
    }
}
