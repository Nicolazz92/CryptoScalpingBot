package com.binance.bot.binance;

import lombok.Getter;

@Getter
public enum Coins {
    XRP_USD("https://www.investing.com/crypto/xrp/xrp-usd?cid=1075586", "XRPUSDT");

    private final String url;
    private final String symbol;

    Coins(String url, String symbol) {
        this.url = url;
        this.symbol = symbol;
    }
}
