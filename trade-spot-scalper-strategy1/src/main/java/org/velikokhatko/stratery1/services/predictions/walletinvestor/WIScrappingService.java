package org.velikokhatko.stratery1.services.predictions.walletinvestor;

import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.velikokhatko.stratery1.exceptions.TraderBotException;
import org.velikokhatko.stratery1.services.api.exchange.ExchangeInfoService;
import org.velikokhatko.stratery1.services.predictions.Prediction;
import org.velikokhatko.stratery1.services.predictions.ScrappingService;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

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
            final String url = FORECAST_URL + baseAsset;
            Document document = Jsoup.connect(url).get();
            final List<Element> elements = document
                    .getElementsByClass("table-cell-label kv-align-right kv-align-middle w0").stream()
                    .filter(e -> "1".equals(e.attributes().get("data-col-seq")))
                    .flatMap(es -> es.children().stream())
                    .flatMap(es -> es.children().stream()).collect(Collectors.toList());
            boolean isUp;
            if (elements.stream().anyMatch(e -> e.hasClass("glyphicon-menu-up"))) {
                isUp = true;
            } else if (elements.stream().anyMatch(e -> e.hasClass("glyphicon-menu-down"))) {
                isUp = false;
            } else {
                throw new TraderBotException("Не удалось спарсить " + url);
            }
            return new Prediction(isUp, predictionHoursTTL);
        } catch (IOException | TraderBotException e) {
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
