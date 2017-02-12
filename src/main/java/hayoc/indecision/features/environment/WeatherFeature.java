package hayoc.indecision.features.environment;

import hayoc.indecision.features.Feature;
import org.apache.log4j.Logger;

import java.time.LocalDateTime;

/**
 * Created by Hayo on 11/02/2017.
 */
public final class WeatherFeature implements Feature {

    private static final double WEATHER_WEIGHT = 1.414;

    @Override
    public double getValue(String user, String option) {
        //-30 min possible value, we want data between 0-90
        return (WeatherWeightCalculator.getWeatherValue() + 30) * WEATHER_WEIGHT;
    }
}
