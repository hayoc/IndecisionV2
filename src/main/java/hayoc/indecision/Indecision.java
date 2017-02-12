package hayoc.indecision;

import hayoc.indecision.decision.Decide;
import hayoc.indecision.util.PropertyReader;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Hayo on 12/02/2017.
 */
public class Indecision {

    public static void main(String[] args) {
        new Indecision().test();
    }

    public void test() {
        List<String> options = new ArrayList<>();
        options.add("blah");
        options.add("booh");

        Decide decide = new Decide();
        String chosen = decide.decide(options, "hayotest");
        System.out.println(chosen);

    }
}
