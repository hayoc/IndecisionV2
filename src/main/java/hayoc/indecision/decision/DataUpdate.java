package hayoc.indecision.decision;

import hayoc.indecision.util.PropertyReader;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

/**
 * Created by Hayo on 12/02/2017.
 */
public class DataUpdate {

    private static final Logger LOG = Logger.getLogger(DataUpdate.class);
    private static final PropertyReader PATHS = new PropertyReader("application.properties");

    public void append(String user, double[] features, boolean result) {
        StringBuilder sb = new StringBuilder();
        for (Double feature : features) {
            sb.append(String.valueOf(feature));
            sb.append(",");
        }
        sb.append(String.valueOf(result));

        try {
            String path = PATHS.getProperty("USER_FEATURES") + PATHS.getProperty("DELIMITER") + user + PATHS.getProperty("DATA_FORMAT");
            Files.write(Paths.get(path), sb.toString().getBytes(), StandardOpenOption.APPEND);
        } catch (IOException e) {
            LOG.error("Failed to append chosen option to user data file.");
        }
    }
}
