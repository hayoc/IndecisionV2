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
        createUser("hayotest");
        for (int i = 0; i < 10; i++) {
            List<String> optionz = getOptions(i);
            updateUserDataWithDecision(optionz, optionz.get(1), "hayotest");
        }

        List<String> options = new ArrayList<>();
        options.add("Should I get a dog, which I hate");
        options.add("Should I get a cat, which I love");
        String chosen = decide(options, "hayotest");
        System.out.println(chosen);
        updateUserDataWithDecision(options, "Should I get a cat, which I love", "hayotest");
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
