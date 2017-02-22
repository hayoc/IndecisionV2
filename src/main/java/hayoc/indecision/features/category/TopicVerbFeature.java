package hayoc.indecision.features.category;

import edu.stanford.nlp.ling.TaggedWord;
import edu.stanford.nlp.parser.lexparser.LexicalizedParser;
import edu.stanford.nlp.trees.Tree;
import hayoc.indecision.features.Feature;
import java_cup.parser;
import org.apache.commons.lang3.StringUtils;

/**
 * Created by Hayo on 22/02/2017.
 */
public class TopicVerbFeature implements Feature {

    private static WordSimilarity wordSimilarity = new WordSimilarity();
    private static LexicalizedParser parser = LexicalizedParser.loadModel();
    private static final String VERB = "VB";
    private static final String REFERENCE = "do";

    @Override
    public double getValue(String user, String option) {
        Tree tree = parser.apply(parser.tokenize(option));

        String verb = StringUtils.EMPTY;

        for (TaggedWord taggedWord : tree.taggedYield()) {
            if (StringUtils.equals(taggedWord.tag(), VERB)) {
                verb = taggedWord.word();
            }
        }

        return wordSimilarity.getValue(REFERENCE, verb);
    }
}
