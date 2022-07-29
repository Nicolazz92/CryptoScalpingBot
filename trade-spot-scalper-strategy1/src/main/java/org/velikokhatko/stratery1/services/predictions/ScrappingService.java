package org.velikokhatko.stratery1.services.predictions;


import velikokhatko.dto.PredictionDTO;

import java.util.Optional;

public interface ScrappingService {

    /**
     * Пример: service.canBuy("XRPBUSD");
     * Даёт прогноз по вылютной паре, основываясь на базовом активе (левом)
     */
    Optional<PredictionDTO> getPrediction(String symbol);
}
