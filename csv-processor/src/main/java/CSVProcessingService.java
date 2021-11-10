import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import model.MarketInterval;
import org.springframework.core.io.ClassPathResource;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.TreeMap;

public class CSVProcessingService {

    public Map<LocalDateTime, MarketInterval> parseCsvIntervals(String fileName) {
        Map<LocalDateTime, MarketInterval> intervals = new TreeMap<>();
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(new ClassPathResource(fileName).getFile()))) {
            final CSVReader csvReader = new CSVReader(bufferedReader);
            String[] line;
            while ((line = csvReader.readNext()) != null) {
                final MarketInterval marketInterval = new MarketInterval(line);
                intervals.put(marketInterval.getOpenTime(), marketInterval);
                System.out.println(marketInterval.getOpenTime().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
            }
        } catch (IOException | CsvValidationException e) {
            e.printStackTrace();
        }
        return intervals;
    }
}
