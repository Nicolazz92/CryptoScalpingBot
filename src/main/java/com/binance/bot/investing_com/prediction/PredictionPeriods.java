package com.binance.bot.investing_com.prediction;

public enum PredictionPeriods {
    FIVE_MINUTES(5),
    FIFTEEN_MINUTES(15),
    HOURLY(60),
    DAILY(1440),
    MONTHLY(43800);

    private final int minutes;

    PredictionPeriods(int minutes) {
        this.minutes = minutes;
    }

    public int getMinutes() {
        return minutes;
    }
}
