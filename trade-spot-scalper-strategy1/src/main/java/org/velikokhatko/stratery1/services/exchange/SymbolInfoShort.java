package org.velikokhatko.stratery1.services.exchange;

import com.binance.api.client.domain.general.SymbolInfo;

public final class SymbolInfoShort {
    public final String symbol;
    public final String baseAsset;
    public final String quoteAsset;

    public SymbolInfoShort(SymbolInfo symbolInfo) {
        this.symbol = symbolInfo.getSymbol();
        this.baseAsset = symbolInfo.getBaseAsset();
        this.quoteAsset = symbolInfo.getQuoteAsset();
    }
}
