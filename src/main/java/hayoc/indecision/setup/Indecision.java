package hayoc.indecision.setup;

import hayoc.indecision.decision.Decision;
import hayoc.indecision.initialization.Initializer;
import org.apache.commons.codec.digest.DigestUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Hayo on 12/02/2017.
 */
public class Indecision {

    private Decision decision;
    private Initializer initializer;

    public Indecision() {
        Distributor distributor = Distributor.setup();
        decision = distributor.getDecision();
        initializer = distributor.getInitializer();
    }

    public static void main(String[] args) {
        new Indecision().execute();
    }

    private void execute() {
        List<String> options = new ArrayList<>();
        options.add("blah");
        options.add("booh");
        String chosen = decision.decide(options, "hayotest");
        System.out.println(chosen);
    }

    public String decide(List<String> options, String user) {
        return decision.decide(options, md5(user));
    }

    public void updateUserDataWithDecision(List<String> options, String chosen, String user) {
        decision.update(options, chosen, md5(user));
    }

    public boolean createUser(String user) {
        return initializer.createUser(md5(user));
    }

    public List<String> getOptions(int index) {
        return initializer.getOptions(index);
    }

    private String md5(String input) {
        return DigestUtils.md5Hex(input);
    }
}
