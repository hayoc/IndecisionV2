package hayoc.indecision.learning.classification;

import weka.classifiers.Classifier;
import weka.core.Instances;

/**
 * Created by Hayo on 11/02/2017.
 */
public interface Classification {

    Classifier build(Instances train, Instances test);

    double classify(Classifier classifier, Instances train, double[] features);
}
