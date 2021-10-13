import org.simpleflatmapper.csv.CsvParser;
import org.simpleflatmapper.lightningcsv.CsvReader;

import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.Iterator;

public class GenerateSystemWithAllTC {

    public static void main(String[] args) throws IOException {
        CsvReader reader = CsvParser.separator(';').reader(new FileReader("export.csv"));

        Iterator<String[]> iterator = reader.iterator();
        String[] headers = iterator.next();
        int codeColumnId = Arrays.asList(headers).indexOf("Код Технологические элементы(value)");
        System.out.printf("");
    }
}