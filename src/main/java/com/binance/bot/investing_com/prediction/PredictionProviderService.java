package com.binance.bot.investing_com.prediction;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.text.ParseException;
import java.util.EnumMap;
import java.util.Optional;

public class PredictionProviderService {

    public Optional<EnumMap<PredictionPeriods, PredictionCases>> getPredictions(final String url) {
        try {
            Document document = getDocument(url);
            Elements classLeftElements = getElements(document);
            EnumMap<PredictionPeriods, PredictionCases> predictions = fillPredictionMap(classLeftElements);
            predictions.forEach((key, val) -> System.out.println(key + " - " + val));

            return Optional.of(predictions);
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }

        return Optional.empty();
    }

    private Document getDocument(final String url) throws IOException {
        Document document = Jsoup.connect(url).get();
        System.out.println(document.title());
        return document;
    }

    private Elements getElements(Document document) {
        Element body = document.body();
        Elements technicalSummaryTblElements = body.getElementsByClass("technicalSummaryTbl");
        Elements tagNameTbodyElements = technicalSummaryTblElements.tagName("tbody");
        Elements regexpSummaryElements = tagNameTbodyElements.get(0).getElementsMatchingText("Summary");
        return regexpSummaryElements.get(2).getElementsByClass("left");
    }

    private EnumMap<PredictionPeriods, PredictionCases> fillPredictionMap(Elements classLeftElements) throws ParseException {
        EnumMap<PredictionPeriods, PredictionCases> predictions = new EnumMap<>(PredictionPeriods.class);
        predictions.put(PredictionPeriods.FIVE_MINUTES, getPredictionCase(1, classLeftElements));
        predictions.put(PredictionPeriods.FIFTEEN_MINUTES, getPredictionCase(2, classLeftElements));
        predictions.put(PredictionPeriods.HOURLY, getPredictionCase(3, classLeftElements));
        predictions.put(PredictionPeriods.DAILY, getPredictionCase(4, classLeftElements));
        predictions.put(PredictionPeriods.MONTHLY, getPredictionCase(5, classLeftElements));
        return predictions;
    }

    private PredictionCases getPredictionCase(int elemNum, Elements left) throws ParseException {
        String parsedValue = left.get(elemNum).textNodes()
                .stream().findFirst().orElseThrow(() ->
                        new ParseException("Не удалось спарсить элемент + " + left.toString(), 0)).text();
        return PredictionCases.ofPattern(parsedValue);
    }
}
