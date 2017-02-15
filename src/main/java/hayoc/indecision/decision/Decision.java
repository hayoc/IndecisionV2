package hayoc.indecision.decision;

import hayoc.indecision.decision.data.FileUpdate;
import hayoc.indecision.features.Feature;
import hayoc.indecision.features.Features;
import hayoc.indecision.features.category.CategoryFeature;
import hayoc.indecision.learning.classification.Classification;
import hayoc.indecision.learning.classification.SVMClassification;
import hayoc.indecision.util.PropertyReader;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import weka.classifiers.Classifier;
import weka.core.Instances;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.Normalize;

import javax.inject.Inject;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by Hayo on 11/02/2017.
 */
public class Decision {

    private static final Logger LOG = Logger.getLogger(Decision.class);
    private static final PropertyReader PATHS = new PropertyReader("application.properties");

    private Classification classification;
    private FileUpdate fileUpdate;

    @Inject
    public Decision(Classification classification, FileUpdate fileUpdate) {
        this.classification = classification;
        this.fileUpdate = fileUpdate;
    }

    public String decide(List<String> options, String user) {
        Instances data = getUserData(user);
        data = prepareData(data, user);
        if (data == null) return StringUtils.EMPTY;

        int amount = data.numInstances();
        int trainSize = (int) Math.round(amount * SVMClassification.SPLIT_PERCENTAGE);
        int testSize = amount - trainSize;

        Instances train = new Instances(data, 0, trainSize);
        Instances test = new Instances(data, trainSize, testSize);

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
        CategoryFeature categoryFeature = new CategoryFeature();
        for (String option : options) {
            boolean result = StringUtils.equals(option, chosen);
            categoryFeature.update(user, option, result);
            fileUpdate.append(user, getFeatures(user, option), result);
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
            LOG.error("Failed to prepare data for user: " + user);
            return data;
        }
    }

    private Instances getUserData(String user) {
        String path = PATHS.getProperty("USER_FEATURES") + File.separator + user + PATHS.getProperty("DATA_FORMAT");
        try (BufferedReader reader = new BufferedReader(new FileReader(path))) {
            return new Instances(reader);
        } catch (IOException e) {
            LOG.error("Failed to read data for user: " + user);
            LOG.error("User has to be initialized first.");
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
