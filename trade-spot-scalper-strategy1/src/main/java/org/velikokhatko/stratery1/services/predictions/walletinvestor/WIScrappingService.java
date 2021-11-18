package org.velikokhatko.stratery1.services.predictions.walletinvestor;

import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.velikokhatko.stratery1.services.exchange.ExchangeInfoService;
import org.velikokhatko.stratery1.services.predictions.Prediction;
import org.velikokhatko.stratery1.services.predictions.ScrappingService;

import java.io.IOException;

@Slf4j
@Service
public class WIScrappingService implements ScrappingService {

    private static final String FORECAST_URL = "https://walletinvestor.com/forecast?currency=";
    private ExchangeInfoService exchangeInfoService;
    private int predictionHoursTTL;

    @Override
    public Prediction getPrediction(String symbol) {
        final String baseAsset = exchangeInfoService.getBaseAsset(symbol);
        if (baseAsset == null) {
            return new Prediction(false, 1);
        }

        try {
            Document document = Jsoup.connect(FORECAST_URL + baseAsset).get();
//            Elements classLeftElements = getElements(document);
//            EnumMap<PredictionPeriods, PredictionCases> predictions = fillPredictionMap(classLeftElements);
//            predictions.forEach((key, val) -> System.out.println(key + " - " + val));
//
//            return null;
            final boolean b = false;
            return new Prediction(b, predictionHoursTTL);
        } catch (IOException e) {
            log.error(e.getMessage());
            return new Prediction(false, 1);
        }
    }

    @Autowired
    public void setExchangeInfoService(ExchangeInfoService exchangeInfoService) {
        this.exchangeInfoService = exchangeInfoService;
    }

    @Value("${walletinvestor.prediction.ttl.hours}")
    public void setPredictionHoursTTL(int predictionHoursTTL) {
        this.predictionHoursTTL = predictionHoursTTL;
    }
}
