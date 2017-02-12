package hayoc.indecision.setup;

import hayoc.indecision.decision.Decision;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Hayo on 12/02/2017.
 */
public class Indecision {

    public static void main(String[] args) {
        new Indecision().execute();
    }

    private void execute() {
        List<String> options = new ArrayList<>();
        options.add("blah");
        options.add("booh");

        Distributor distributor = Distributor.setup();
        Decision decision = distributor.getDecision();
        String chosen = decision.decide(options, "hayotest");
        System.out.println(chosen);
    }
}
