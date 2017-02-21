package hayoc.indecision.features.environment;

import org.junit.Test;

import static junit.framework.TestCase.assertTrue;

/**
 * Created by Hayo on 21/02/2017.
 */
public class EnvironmentFeaturesTest {

    private static final String USER = "test";
    private static final String OPTION = "an option";

    @Test
    public void testDayFeature() {
        double value = new DayFeature().getValue(USER, OPTION);
        assertTrue(value < 100 && value > 0);
    }

    @Test
    public void testMonthFeature() {
        double value = new MonthFeature().getValue(USER, OPTION);
        assertTrue(value < 100 && value > 0);
    }

    @Test
    public void testTimeFeature() {
        double value = new TimeFeature().getValue(USER, OPTION);
        assertTrue(value < 100 && value > 0);
    }

    @Test
    public void testWeatherFeature() {
        double value = new WeatherFeature().getValue(USER, OPTION);
        assertTrue(value < 100 && value > 0);
    }
}
