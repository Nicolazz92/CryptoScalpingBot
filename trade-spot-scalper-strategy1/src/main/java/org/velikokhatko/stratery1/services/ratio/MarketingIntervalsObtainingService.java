package org.velikokhatko.stratery1.services.ratio;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Service;
import org.velikokhatko.stratery1.services.ratio.model.MarketInterval;

import java.io.*;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

@Service
@Slf4j
public class MarketingIntervalsObtainingService {

    public Map<LocalDateTime, MarketInterval> obtainCsvIntervals(List<String> urls) {
        Map<LocalDateTime, MarketInterval> intervals = new HashMap<>();
        for (String url : urls) {
            log.info("Начало обработки исторических данных: {}", url);
            final Map<LocalDateTime, MarketInterval> buffer = loadMarketIntervalsFromRemote(url);
            log.info("Конец обработки исторических данных: {}; Значений: {}; интервал [{} - {}]",
                    url,
                    buffer.size(),
                    buffer.keySet().stream().min(LocalDateTime::compareTo).orElseThrow(),
                    buffer.keySet().stream().max(LocalDateTime::compareTo).orElseThrow()
            );
            intervals.putAll(buffer);
        }
        return intervals;
    }

    private Map<LocalDateTime, MarketInterval> loadMarketIntervalsFromRemote(String url) {
        List<File> extractFiles = new ArrayList<>();
        Map<LocalDateTime, MarketInterval> buffer = new TreeMap<>();

        try (InputStream is = new URL(url).openConnection().getInputStream();
             BufferedInputStream bis = new BufferedInputStream(is);
             ZipInputStream zis = new ZipInputStream(bis)
        ) {
            extractFiles.addAll(extractFileToTemp(zis));
            for (File extractFile : extractFiles) {
                try (BufferedReader bufferedReader = new BufferedReader(new FileReader(extractFile))) {
                    final CSVReader csvReader = new CSVReader(bufferedReader);
                    String[] line;
                    while ((line = csvReader.readNext()) != null) {
                        final MarketInterval marketInterval = new MarketInterval(line);
                        buffer.put(marketInterval.getOpenTime(), marketInterval);
                    }
                }
            }
        } catch (IOException | CsvValidationException e) {
            log.error("Не удалось получить исторические данные по url={}", url, e);
            e.printStackTrace();
        } finally {
            for (File extractFile : extractFiles) {
                if (!extractFile.delete()) {
                    log.error("Не удалось удалить временный файл {}", extractFile.getAbsolutePath());
                }
            }
        }
        return buffer;
    }

    private List<File> extractFileToTemp(ZipInputStream zipInputStream) throws IOException {
        List<File> extractedTempFiles = new ArrayList<>();

        ZipEntry zipEntry;
        while ((zipEntry = zipInputStream.getNextEntry()) != null) {
            File unZippedFile = new File(FileUtils.getTempDirectory() + File.separator + zipEntry.getName());
            writeContents(zipInputStream, unZippedFile);
            extractedTempFiles.add(unZippedFile);
        }

        return extractedTempFiles;
    }

    private static void writeContents(ZipInputStream zipInputStream, File outputFile) throws IOException {
        try (FileOutputStream fileOutputStream = new FileOutputStream(outputFile);) {
            int len;
            byte[] content = new byte[1024];
            while ((len = zipInputStream.read(content)) > 0) {
                fileOutputStream.write(content, 0, len);
            }
        }
    }
}
