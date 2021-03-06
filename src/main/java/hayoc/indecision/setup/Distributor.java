package hayoc.indecision.setup;

import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import hayoc.indecision.decision.Decision;
import hayoc.indecision.initialization.Initializer;

/**
 * Created by Hayo on 12/02/2017.
 */
public class Distributor {

    private Decision decision;
    private Initializer initializer;

    @Inject
    public Distributor(Decision decision, Initializer initializer) {
        this.decision = decision;
        this.initializer = initializer;
    }

    public static Distributor setup() {
        IndecisionModule module = new IndecisionModule();
        Injector injector = Guice.createInjector(module);
        return injector.getInstance(Distributor.class);
    }

    public Decision getDecision() {
        return decision;
    }

    public void setDecision(Decision decision) {
        this.decision = decision;
    }

    public Initializer getInitializer() {
        return initializer;
    }

    public void setInitializer(Initializer initializer) {
        this.initializer = initializer;
    }
}
