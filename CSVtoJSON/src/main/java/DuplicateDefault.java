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

public class DuplicateDefault {

    public static void main(String[] args) throws IOException {
        CsvReader reader = CsvParser.separator(';').reader(new FileReader("export.csv"));

        Iterator<String[]> iterator = reader.iterator();
        String[] headers = iterator.next();
        int codeColumnId = Arrays.asList(headers).indexOf("Код Технологические элементы(value)");

        Path files = Path.of("files");
        if (!files.toFile().exists()) {
            Files.createDirectory(files);
        }

        while (iterator.hasNext()) {
            String[] values = iterator.next();
            String technologyCode = values[codeColumnId];
            Files.copy(Path.of("default.png"), Paths.get(files.toString(), technologyCode + ".png"), StandardCopyOption.REPLACE_EXISTING);
        }
    }
}