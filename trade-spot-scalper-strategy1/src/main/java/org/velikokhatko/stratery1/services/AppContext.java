package org.velikokhatko.stratery1.services;

import com.velikokhatko.model.Prediction;
import com.velikokhatko.model.RatioParams;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public final class AppContext {

    public static final Map<String, Prediction> PREDICTION_CACHE = new ConcurrentHashMap<>();
    public static final Map<String, RatioParams> RATIO_CACHE = new ConcurrentHashMap<>();
}
