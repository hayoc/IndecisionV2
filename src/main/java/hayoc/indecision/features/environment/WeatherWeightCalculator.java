package hayoc.indecision.features.environment;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import hayoc.indecision.util.PropertyReader;
import org.apache.log4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

/**
 * Created by Hayo on 11/02/2017.
 */
final class WeatherWeightCalculator {

    private static final Logger LOG = Logger.getLogger(WeatherWeightCalculator.class);
    private static final PropertyReader INFO = new PropertyReader("application.properties");

    private static final String WEATHER_URL = INFO.getProperty("WEATHER_URL");
    private static final String API_KEY = INFO.getProperty("API_KEY");
    private static final String CITY_ID = INFO.getProperty("CITY_ID");
    private static final String UNITS = INFO.getProperty("UNITS");
    private static JsonParser jsonParser = new JsonParser();

    public static double getWeatherValue() {
        JsonObject main = requestWeather().getAsJsonObject("main");
        return (main != null) ? main.get("temp").getAsDouble() : -30;
    }

    private static JsonObject requestWeather() {
        String uri = WEATHER_URL + "appid=" + API_KEY + "&id=" + CITY_ID + "&units=" + UNITS;
        try {
            URL url = new URL(uri);
            BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()));
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
            return jsonParser.parse(sb.toString()).getAsJsonObject();
        } catch (IOException e) {
            LOG.error("Could not connect to : " + uri);
        }
        return new JsonObject();
    }
}
