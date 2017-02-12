package hayoc.indecision.setup;

import com.google.inject.AbstractModule;
import hayoc.indecision.decision.data.DataUpdate;
import hayoc.indecision.decision.data.FileUpdate;
import hayoc.indecision.learning.classification.Classification;
import hayoc.indecision.learning.classification.SVMClassification;

/**
 * Created by Hayo on 12/02/2017.
 */
public class IndecisionModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(Classification.class).to(SVMClassification.class);
        bind(DataUpdate.class).to(FileUpdate.class);
    }
}
