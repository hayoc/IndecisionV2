package hayoc.indecision.initialization;

import hayoc.indecision.decision.data.DataUpdate;
import hayoc.indecision.features.Features;
import hayoc.indecision.util.PropertyReader;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import javax.inject.Inject;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Hayo on 12/02/2017.
 */
public class Initializer {

    private static final Logger LOG = Logger.getLogger(Initializer.class);
    private static final PropertyReader PATHS = new PropertyReader("application.properties");

    public boolean createUser(String user) {
        try {
            String path = PATHS.getProperty("USER_FEATURES") + PATHS.getProperty("DELIMITER") + user + PATHS.getProperty("DATA_FORMAT");
            Files.write(Paths.get(path), defaultContent(), StandardCharsets.UTF_8, StandardOpenOption.CREATE, StandardOpenOption.APPEND);
            return true;
        } catch (IOException e) {
            LOG.error("Failed to create new USER: " + user);
            LOG.error(e.getMessage());
            return false;
        }
    }

    public List<String> getOptions(int index) {
        try {
            Path path = Paths.get(getClass().getClassLoader().getResource("initialization/decisions.txt").toURI());
            List<String> options = Files.readAllLines(path, Charset.defaultCharset());
            if (index * 2 >= options.size()) return Collections.emptyList();
            return options.subList(index * 2, (index * 2) + 1);
        } catch (IOException | URISyntaxException e) {
            LOG.error("Could not find 'decisions.txt' to initialize.");
            return Collections.emptyList();
        }
    }

    private static List<String> defaultContent() {
        List<String> lines = new ArrayList<>();
        lines.add("@RELATION indecision");
        for (Class clazz : Features.LIST) {
            lines.add("@ATTRIBUTE " + clazz.getSimpleName() +  "\t\t NUMERIC");
        }
        lines.add("@ATTRIBUTE decision\t\t{true,false}");
        return lines;
    }
}
