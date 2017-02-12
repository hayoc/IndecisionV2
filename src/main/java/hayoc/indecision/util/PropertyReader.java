package hayoc.indecision.util;

import org.apache.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created by Hayo on 11/02/2017.
 */
public class PropertyReader {

    private static final Logger LOG = Logger.getLogger(PropertyReader.class);

    private Properties properties;

    public PropertyReader(String path) {
        properties = new Properties();
        try {
            InputStream input = getClass().getClassLoader().getResourceAsStream(path);
            properties.load(input);
        } catch (IOException e) {
            LOG.error("Could not read properties from file " + path);
        }
    }

    public String getProperty(String key) {
        return properties.getProperty(key);
    }
}