package hayoc.indecision.features.category;

import edu.cmu.lti.lexical_db.ILexicalDatabase;
import edu.cmu.lti.lexical_db.NictWordNet;
import edu.cmu.lti.ws4j.RelatednessCalculator;
import edu.cmu.lti.ws4j.impl.WuPalmer;
import edu.cmu.lti.ws4j.util.WS4JConfiguration;

/**
 * Created by Hayo on 22/02/2017.
 */
public class WordSimilarity {

    static {
        WS4JConfiguration.getInstance().setMFS(true);
    }

    private static ILexicalDatabase db = new NictWordNet();
    private static RelatednessCalculator rc = new WuPalmer(db);

    public double getValue(String reference, String word) {
        return rc.calcRelatednessOfWords(reference, word);
    }
}
