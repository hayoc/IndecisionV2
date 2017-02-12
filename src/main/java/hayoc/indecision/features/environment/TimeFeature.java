package hayoc.indecision.features.environment;

import hayoc.indecision.features.Feature;

import java.time.LocalDateTime;

/**
 * Created by Hayo on 11/02/2017.
 */
public class TimeFeature implements Feature {

    private static final double TIME_WEIGHT = 2960727.27273;

    @Override
    public double getValue(String user, String option) {
        return ((LocalDateTime.now().getHour() * 3600) * (LocalDateTime.now().getMinute() * 60))/TIME_WEIGHT;
    }
}
