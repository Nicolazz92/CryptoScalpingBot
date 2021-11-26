package org.velikokhatko.stratery1.services.predictions;

import lombok.ToString;
import org.apache.commons.lang3.RandomUtils;

import java.time.LocalDateTime;

@ToString
public final class Prediction {

    public final boolean canBuy;
    public final LocalDateTime freshLimit;

    public Prediction(boolean canBuy, int predictionHoursTTL) {
        this.canBuy = canBuy;
        this.freshLimit = LocalDateTime.now().plusHours(predictionHoursTTL).plusMinutes(RandomUtils.nextLong(0, 240));
    }
}
