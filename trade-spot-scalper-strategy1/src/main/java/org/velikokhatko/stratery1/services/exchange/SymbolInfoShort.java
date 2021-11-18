package org.velikokhatko.stratery1.services.exchange;

import com.binance.api.client.domain.general.FilterType;
import com.binance.api.client.domain.general.SymbolInfo;

public final class SymbolInfoShort {
    public final String symbol;
    public final String baseAsset;
    public final double lotSizeMin;

    public SymbolInfoShort(SymbolInfo symbolInfo) {
        this.symbol = symbolInfo.getSymbol();
        this.baseAsset = symbolInfo.getBaseAsset();
        this.lotSizeMin = Double.parseDouble(symbolInfo.getSymbolFilter(FilterType.LOT_SIZE).getMinQty());
    }
}
