package com.binance.bot.investing_com.prediction;

import java.util.Arrays;

public enum PredictionCases {
    SELL("Sell"),
    STRONG_SELL("Strong Sell"),
    NEUTRAL("Neutral"),
    BUY("Buy"),
    STRONG_BUY("Strong Buy");

    private final String pattern;

    PredictionCases(String pattern) {
        this.pattern = pattern;
    }

    public static PredictionCases ofPattern(String parsedValue) {
        return Arrays.stream(values())
                .filter(val -> val.pattern.equalsIgnoreCase(parsedValue))
                .findAny()
                .orElseThrow(() -> new RuntimeException("Not found " + PredictionCases.class.getSimpleName()
                        + " by parsed value + " + parsedValue));
    }

    public String getPattern() {
        return pattern;
    }
}
