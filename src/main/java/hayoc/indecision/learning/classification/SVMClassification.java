package hayoc.indecision.learning.classification;

import hayoc.indecision.decision.Decide;
import org.apache.log4j.Logger;
import weka.classifiers.*;
import weka.classifiers.Evaluation;
import weka.classifiers.functions.LibSVM;
import weka.core.Instance;
import weka.core.Instances;

import java.util.Random;

/**
 * Created by Hayo on 11/02/2017.
 */
public class SVMClassification implements Classification {

    private static final Logger LOG = Logger.getLogger(SVMClassification.class);

    public static final double SPLIT_PERCENTAGE = 0.9;

    @Override
    public LibSVM build(Instances train, Instances test) {
        LibSVM classifier = new LibSVM();
        classifier.setProbabilityEstimates(true);

        try {
            classifier.buildClassifier(train);
            Evaluation evaluation = new Evaluation(train);
            evaluation.evaluateModel(classifier, test);
            LOG.info("Confidence: " + evaluation.pctCorrect());
            return classifier;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public double classify(Classifier classifier, Instances train, double[] features) {
        Instance instance = new Instance(1.0, features);
        instance.setDataset(train);
        try {
            return classifier.distributionForInstance(instance)[0];
        } catch (Exception e) {
            return 0.0;
        }
    }
}
