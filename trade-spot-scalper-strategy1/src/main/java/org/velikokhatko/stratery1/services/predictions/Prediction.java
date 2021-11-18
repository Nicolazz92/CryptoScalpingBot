package org.velikokhatko.stratery1.services.predictions;

import java.time.LocalDateTime;

public final class Prediction {

    public final boolean canBuy;
    public final LocalDateTime freshLimit;

    public Prediction(boolean canBuy, int predictionHoursTTL) {
        this.canBuy = canBuy;
        freshLimit = LocalDateTime.now().plusHours(predictionHoursTTL);
    }
}
