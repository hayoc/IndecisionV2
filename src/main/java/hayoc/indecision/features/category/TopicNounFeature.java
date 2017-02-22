package hayoc.indecision.features.category;

import edu.stanford.nlp.ling.TaggedWord;
import edu.stanford.nlp.ling.Word;
import edu.stanford.nlp.parser.lexparser.LexicalizedParser;
import edu.stanford.nlp.trees.Tree;
import hayoc.indecision.features.Feature;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

/**
 * Created by Hayo on 22/02/2017.
 */
public class TopicNounFeature implements Feature {

    private static WordSimilarity wordSimilarity = new WordSimilarity();
    private static LexicalizedParser parser = LexicalizedParser.loadModel();
    private static final String NOUN = "NN";
    private static final String REFERENCE = "thing";

    @Override
    public double getValue(String user, String option) {
        Tree tree = parser.apply(parser.tokenize(option));

        String noun = StringUtils.EMPTY;

        for (TaggedWord taggedWord : tree.taggedYield()) {
            if (StringUtils.equals(taggedWord.tag(), NOUN)) {
                noun = taggedWord.word();
            }
        }

        return wordSimilarity.getValue(REFERENCE, noun);
    }
}
