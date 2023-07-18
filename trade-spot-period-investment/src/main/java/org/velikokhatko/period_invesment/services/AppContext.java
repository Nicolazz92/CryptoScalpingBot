package org.velikokhatko.period_invesment.services;

import org.velikokhatko.period_invesment.services.predictions.Prediction;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public final class AppContext {

    public static final Map<String, Prediction> PREDICTION_CACHE = new ConcurrentHashMap<>();
}
