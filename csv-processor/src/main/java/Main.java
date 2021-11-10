import model.MarketInterval;
import service.CSVProcessingService;

import java.time.LocalDateTime;
import java.util.Map;

public class Main {
    private static final CSVProcessingService csvProcessingService = new CSVProcessingService();

    public static void main(String[] args) {
        final String fileName = "FTTBNB-1m-2021-06.csv";
        final Map<LocalDateTime, MarketInterval> marketIntervals = csvProcessingService.parseCsvIntervals(fileName);
        System.out.println(marketIntervals.size());
    }
}
