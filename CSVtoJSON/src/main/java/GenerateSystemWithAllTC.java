import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import model.Kind;
import model.Obj;
import model.Wrapper;
import org.simpleflatmapper.csv.CsvParser;
import org.simpleflatmapper.lightningcsv.CsvReader;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

/**
 * На основании выгрузки из таблицы конфлюенса генерит yaml со всеми существующими технологическими компонентами
 * для отслеживания полноты картинок технологических компонентов
 * https://confluence.sberbank.ru/pages/viewpage.action?pageId=5348365926
 */
public class GenerateSystemWithAllTC {

    public static void main(String[] args) throws IOException {
        CsvReader reader = CsvParser.separator(';').reader(new FileReader("export.csv"));

        Iterator<String[]> iterator = reader.iterator();
        String[] headers = iterator.next();
        int codeColumnId = Arrays.asList(headers).indexOf("Код Технологические элементы(value)");
        int nameColumnId = Arrays.asList(headers).indexOf("Наименование Технологические элементы(label)");

        Obj system = new Obj(getUUID(), null, "Система", Kind.System, null);
        List<Obj> digitalArchitecture = fillSystem(iterator, codeColumnId, nameColumnId, system);

        ObjectMapper objectMapper = new ObjectMapper(new YAMLFactory());
        objectMapper.writeValue(new File("allTechComponents.yaml"), new Wrapper(digitalArchitecture));
    }

    private static List<Obj> fillSystem(Iterator<String[]> iterator, int codeColumnId, int nameColumnId, Obj system) {
        List<Obj> digitalArchitecture = new ArrayList<>();
        digitalArchitecture.add(system);
        while (iterator.hasNext()) {
            String[] next = iterator.next();
            String technologyCode = next[codeColumnId];
            String name = next[nameColumnId];
            Obj techComponent = new Obj(getUUID(), system.getYamlId(), name, Kind.TechnologicalComponent, technologyCode);
            digitalArchitecture.add(techComponent);
        }
        return digitalArchitecture;
    }

    private static String getUUID() {
        return UUID.randomUUID().toString();
    }
}
