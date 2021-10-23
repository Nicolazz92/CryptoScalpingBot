package model;

import java.time.LocalDateTime;

public class MarketInterval {
    LocalDateTime openTime;
    Double open;
    Double High;
    Double Low;
    Double Close;
    Double Volume;
    LocalDateTime closeTime;
    Double QuoteAssetVolume;
    Integer NumberOfTrades;
    Double TakerBuyBaseAssetVolume;
    Double TakerBuyQuoteAssetVolume;
    Double Ignore;
}
