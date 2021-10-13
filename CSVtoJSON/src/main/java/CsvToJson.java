import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import org.simpleflatmapper.csv.CsvParser;
import org.simpleflatmapper.lightningcsv.CsvReader;

import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.Iterator;

public class CsvToJson {

    public static void main(String[] args) throws IOException {
        CsvReader reader = CsvParser.separator(';').reader(new FileReader("export.csv"));

        JsonFactory jsonFactory = new JsonFactory();

        Iterator<String[]> iterator = reader.iterator();
        String[] headers = iterator.next();
        int imageNameColumnNum = Arrays.asList(headers).indexOf("Наименование изображения");
        headers[imageNameColumnNum] = "image";
        headers[Arrays.asList(headers).indexOf("Код Технологические элементы(value)")] = "code";
        int alternativeImageNameColumnNum = Arrays.asList(headers).indexOf("Другие варианты изображения");
        headers[alternativeImageNameColumnNum] = "image_alternative";
        //code
        int ready4ProdColumnNum = Arrays.asList(headers).indexOf("Готово к проду");

        try (JsonGenerator jsonGenerator = jsonFactory.createGenerator(new FileOutputStream("techCompConflExport.json"))) {

            jsonGenerator.writeStartArray();

            while (iterator.hasNext()) {
                String[] values = iterator.next();
                if (!"+".equals(values[ready4ProdColumnNum])) {
                    if (iterator.hasNext()) {
                        continue;
                    } else {
                        break;
                    }
                }
                try {
                    jsonGenerator.writeStartObject();
                    values[imageNameColumnNum] = "image=img/lib/sber/{IMAGE};imageWidth=48;imageHeight=48;".replaceAll("\\{IMAGE}",
                            values[imageNameColumnNum]);
                    values[alternativeImageNameColumnNum] = "image=img/lib/sber/{IMAGE};imageWidth=48;imageHeight=48;".replaceAll("\\{IMAGE}",
                            values[alternativeImageNameColumnNum]);
                    int nbCells = Math.min(values.length, headers.length);
                    for (int i = 0; i < nbCells; i++) {
                        jsonGenerator.writeFieldName(headers[i]);
                        jsonGenerator.writeString(values[i]);
                    }
                } finally {
                    jsonGenerator.writeEndObject();
                }
            }
            jsonGenerator.writeEndArray();
        }
    }
}