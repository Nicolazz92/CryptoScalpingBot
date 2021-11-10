package model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

@Getter
@Setter
@ToString
public class MarketInterval {
    private LocalDateTime openTime;
    private Double open;
    private Double high;
    private Double low;
    private Double close;
    private Double volume;
    private LocalDateTime closeTime;
    private Double quoteAssetVolume;
    private Integer numberOfTrades;
    private Double takerBuyBaseAssetVolume;
    private Double takerBuyQuoteAssetVolume;
    private Double ignore;

    public MarketInterval(String[] line) {
        this.openTime = Instant.ofEpochMilli(Long.parseLong(line[0])).atZone(ZoneId.systemDefault()).toLocalDateTime();
        this.open = Double.valueOf(line[1]);
        this.high = Double.valueOf(line[2]);
        this.low = Double.valueOf(line[3]);
        this.close = Double.valueOf(line[4]);
        this.volume = Double.valueOf(line[5]);
        this.closeTime = Instant.ofEpochMilli(Long.parseLong(line[6])).atZone(ZoneId.systemDefault()).toLocalDateTime();
        this.quoteAssetVolume = Double.valueOf(line[7]);
        this.numberOfTrades = Integer.valueOf(line[8]);
        this.takerBuyBaseAssetVolume = Double.valueOf(line[9]);
        this.takerBuyQuoteAssetVolume = Double.valueOf(line[10]);
        this.ignore = Double.valueOf(line[11]);
    }
}
