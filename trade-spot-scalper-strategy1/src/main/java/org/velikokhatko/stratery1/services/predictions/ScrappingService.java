package org.velikokhatko.stratery1.services.predictions;

public interface ScrappingService {

    /**
     * Пример: service.canBuy("XRPBUSD");
     * Даёт прогноз по вылютной паре, основываясь на базовом активе (левом)
     */
    Prediction getPrediction(String symbol);
}
