package hayoc.indecision.features;

import hayoc.indecision.features.environment.DayFeature;
import hayoc.indecision.features.environment.MonthFeature;
import hayoc.indecision.features.environment.TimeFeature;
import hayoc.indecision.features.environment.WeatherFeature;
import org.apache.log4j.Logger;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Hayo on 11/02/2017.
 */
public class Features {

    private static final Logger LOG = Logger.getLogger(Features.class);

    public static List<Feature> ALL;

    private static Class[] LIST = {DayFeature.class, MonthFeature.class, TimeFeature.class, WeatherFeature.class};

    static {
        ALL = getAllFeatures();
    }

    private static List<Feature> getAllFeatures() {
        List<Feature> features = new ArrayList<>();
        for (Class<?> clazz : Features.LIST) {
            try {
                Constructor<?> constructor = clazz.getConstructor();
                Feature feature = (Feature) constructor.newInstance();
                features.add(feature);
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                LOG.error("Could not instantiate " + clazz.getSimpleName() + " with error: " + e);
            }
        }
        return features;
    }
}
