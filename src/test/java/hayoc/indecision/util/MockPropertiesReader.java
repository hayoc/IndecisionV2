package hayoc.indecision.util;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Hayo on 22/02/2017.
 */
public class MockPropertiesReader implements Reader {

    private Map<String, String> properties = new HashMap<>();

    public void setProperty(String key, String property) {
        properties.put(key, property);
    }

    @Override
    public String getProperty(String key) {
        return properties.get(key);
    }
}
