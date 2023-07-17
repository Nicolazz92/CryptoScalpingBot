package org.velikokhatko.stratery1.services.api.exchange;

import com.binance.api.client.domain.general.FilterType;
import com.binance.api.client.domain.general.SymbolInfo;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public final class SymbolInfoShort {
    private String symbol;
    private String baseAsset;
    private double lotSizeMin;
    private double marketLotSizeMin;

    public SymbolInfoShort(SymbolInfo symbolInfo) {
        this.symbol = symbolInfo.getSymbol();
        this.baseAsset = symbolInfo.getBaseAsset();
        this.lotSizeMin = Double.parseDouble(symbolInfo.getSymbolFilter(FilterType.LOT_SIZE).getMinQty());
        this.marketLotSizeMin = Double.parseDouble(symbolInfo.getSymbolFilter(FilterType.MARKET_LOT_SIZE).getMinQty());
    }
}
