package org.velikokhatko.stratery1.services;


import velikokhatko.dto.PredictionDTO;
import velikokhatko.dto.RatioParamsDTO;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public final class AppContext {

    public static final Map<String, PredictionDTO> PREDICTION_CACHE = new ConcurrentHashMap<>();
    public static final Map<String, RatioParamsDTO> RATIO_CACHE = new ConcurrentHashMap<>();
}
