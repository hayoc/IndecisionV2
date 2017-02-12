package hayoc.indecision.features.environment;

import hayoc.indecision.features.Feature;

import java.time.LocalDateTime;

/**
 * Created by Hayo on 11/02/2017.
 */
public class DayFeature implements Feature {

    private static final double DAY_WEIGHT = 14.142;

    @Override
    public double getValue(String user, String option) {
        return LocalDateTime.now().getDayOfWeek().getValue() * DAY_WEIGHT;
    }
}
