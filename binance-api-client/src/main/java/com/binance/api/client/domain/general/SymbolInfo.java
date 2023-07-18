package com.binance.api.client.domain.general;

import com.binance.api.client.constant.BinanceApiConstants;
import com.binance.api.client.domain.OrderType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * Symbol information (base/quote).
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class SymbolInfo {

    public String symbol;
    public SymbolStatus status;
    public String baseAsset;
    public Integer baseAssetPrecision;
    public String quoteAsset;
    public Integer quotePrecision;
    public Integer quoteAssetPrecision;
    public Boolean icebergAllowed;
    public Boolean ocoAllowed;
    public Boolean quoteOrderQtyMarketAllowed;
    public Boolean allowTrailingStop;
    public Boolean cancelReplaceAllowed;
    public Boolean isSpotTradingAllowed;
    public Boolean isMarginTradingAllowed;
    public ArrayList<String> permissions;
    public String defaultSelfTradePreventionMode;
    public ArrayList<String> allowedSelfTradePreventionModes;

    private List<OrderType> orderTypes;
    private List<SymbolFilter> filters;

    /**
     * @param filterType filter type to filter for.
     * @return symbol filter information for the provided filter type.
     */
    public SymbolFilter getSymbolFilter(FilterType filterType) {
        return filters.stream()
                .filter(symbolFilter -> symbolFilter.getFilterType() == filterType)
                .findFirst()
                .get();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, BinanceApiConstants.TO_STRING_BUILDER_STYLE)
                .append("symbol", symbol)
                .append("status", status)
                .append("baseAsset", baseAsset)
                .append("baseAssetPrecision", baseAssetPrecision)
                .append("quoteAsset", quoteAsset)
                .append("quotePrecision", quotePrecision)
                .append("orderTypes", orderTypes)
                .append("icebergAllowed", icebergAllowed)
                .append("filters", filters)
                .toString();
    }
}
