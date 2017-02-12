package hayoc.indecision.features.environment;

import hayoc.indecision.features.Feature;

import java.time.LocalDateTime;

/**
 * Created by Hayo on 11/02/2017.
 */
public class MonthFeature implements Feature {

    private static final double MONTH_WEIGHT = 8.25;

    @Override
    public double getValue(String user, String option) {
        return LocalDateTime.now().getMonthValue() * MONTH_WEIGHT;
    }
}
