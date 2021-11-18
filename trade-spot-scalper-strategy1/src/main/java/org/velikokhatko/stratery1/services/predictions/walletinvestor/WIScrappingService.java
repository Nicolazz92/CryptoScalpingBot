package org.velikokhatko.stratery1.services.predictions.walletinvestor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.velikokhatko.stratery1.services.predictions.Prediction;
import org.velikokhatko.stratery1.services.predictions.ScrappingService;

@Service
public class WIScrappingService implements ScrappingService {

    private int predictionDaysTTL;

    @Override
    public Prediction getPrediction(String symbol) {
        return null;
    }

    @Value("${walletinvestor.prediction.ttl.days}")
    public void setPredictionDaysTTL(int predictionDaysTTL) {
        this.predictionDaysTTL = predictionDaysTTL;
    }
}
