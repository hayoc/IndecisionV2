package hayoc.indecision.decision;

import hayoc.indecision.features.Feature;
import hayoc.indecision.features.Features;
import hayoc.indecision.learning.classification.Classification;
import hayoc.indecision.learning.classification.SVMClassification;
import hayoc.indecision.util.PropertyReader;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import weka.classifiers.Classifier;
import weka.core.Instances;
import weka.core.converters.ConverterUtils;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.Normalize;

import javax.inject.Inject;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by Hayo on 11/02/2017.
 */
public class Decide {

    private static final Logger LOG = Logger.getLogger(Decide.class);
    private static final PropertyReader PATHS = new PropertyReader("application.properties");

    private DataUpdate dataUpdate;

    @Inject
    public Decide(DataUpdate dataUpdate) {
        this.dataUpdate = dataUpdate;
    }

    public String decide(List<String> options, String user) {
        Instances data = null;
        data = getUserData(user);
        data = prepareData(data, user);
        if (data == null) return StringUtils.EMPTY;

        int amount = data.numInstances();
        int trainSize = (int) Math.round(amount * SVMClassification.SPLIT_PERCENTAGE);
        int testSize = amount - trainSize;

        Instances train = new Instances(data, 0, trainSize);
        Instances test = new Instances(data, trainSize, testSize);

        Classification classification = new SVMClassification();
        Classifier classifier = classification.build(train, test);

        String decision = StringUtils.EMPTY;
        double highest = 0.0;

        for (String option : options) {
            double score = classification.classify(classifier, train, getFeatures(user, option));

            if (score > highest) {
                if (highest != 0.0 && score == highest) {
                    LOG.warn("Found options with equal score");
                }
                highest = score;
                decision = option;
            }
        }

        return (highest == 0.0) ? StringUtils.EMPTY : decision;
    }

    public void update(List<String> options, String chosen, String user) {
        for (String option : options) {
            dataUpdate.append(user, getFeatures(user, option), StringUtils.equals(option, chosen));
        }
    }

    private Instances prepareData(Instances data, String user) {
        try {
            data.setClassIndex(data.numAttributes() - 1);
            data.randomize(new Random(0));

            Filter filter = new Normalize();
            filter.setInputFormat(data);
            return Filter.useFilter(data, filter);
        } catch (Exception e) {
            LOG.error("Failed to read/prepare data for user: " + user);
            return data;
        }
    }

    private Instances getUserData(String user) {
        InputStream stream = getClass().getClassLoader().getResourceAsStream(PATHS.getProperty("USER_FEATURES") + PATHS.getProperty("DELIMITER") + user + PATHS.getProperty("DATA_FORMAT"));
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(stream))) {
            return new Instances(reader);
        } catch (IOException e) {
            LOG.error("Failed to read data for user: " + user);
            return null;
        }
    }

    private double[] getFeatures(String user, String option) {
        List<Double> list = new ArrayList<>();
        for (Feature feature : Features.ALL) {
            list.add(feature.getValue(user, option));
        }

        return list.stream().mapToDouble(Double::doubleValue).toArray();
    }
}
