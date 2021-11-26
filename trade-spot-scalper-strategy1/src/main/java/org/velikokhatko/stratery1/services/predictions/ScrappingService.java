package org.velikokhatko.stratery1.services.predictions;

import java.util.Optional;

public interface ScrappingService {

    /**
     * Пример: service.canBuy("XRPBUSD");
     * Даёт прогноз по вылютной паре, основываясь на базовом активе (левом)
     */
    Optional<Prediction> getPrediction(String symbol);
}
