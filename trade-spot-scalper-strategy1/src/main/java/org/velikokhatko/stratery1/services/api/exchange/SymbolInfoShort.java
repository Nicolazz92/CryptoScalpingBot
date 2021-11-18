package org.velikokhatko.stratery1.services.api.exchange;

import com.binance.api.client.domain.general.FilterType;
import com.binance.api.client.domain.general.SymbolInfo;

public final class SymbolInfoShort {
    public final String symbol;
    public final String baseAsset;
    public final double lotSizeMin;
    public final double marketLotSizeMin;

    public SymbolInfoShort(SymbolInfo symbolInfo) {
        this.symbol = symbolInfo.getSymbol();
        this.baseAsset = symbolInfo.getBaseAsset();
        this.lotSizeMin = Double.parseDouble(symbolInfo.getSymbolFilter(FilterType.LOT_SIZE).getMinQty());
        this.marketLotSizeMin = Double.parseDouble(symbolInfo.getSymbolFilter(FilterType.MARKET_LOT_SIZE).getMinQty());
    }
}
