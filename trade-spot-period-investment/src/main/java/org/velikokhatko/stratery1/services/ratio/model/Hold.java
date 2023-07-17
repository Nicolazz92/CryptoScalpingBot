package org.velikokhatko.stratery1.services.ratio.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
public class Hold {
    private Double buyingPrice;
    private Double expectingPrice;
    private Double moneyAmount;
    private LocalDateTime buyingDate;
    private LocalDateTime sellingDate;

    public Hold(Double buyingPrice, Double expectingPrice, Double moneyAmount) {
        this.buyingPrice = buyingPrice;
        this.expectingPrice = expectingPrice;
        this.moneyAmount = moneyAmount;
    }
}
