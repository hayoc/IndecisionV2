package hayoc.indecision.features.category;

import org.junit.Test;

import static junit.framework.TestCase.assertTrue;

/**
 * Created by Hayo on 22/02/2017.
 */
public class TopicVerbFeatureTest {

    @Test
    public void testTopicVerbFeature() {
        double value = new TopicVerbFeature().getValue("testuser", "Should I get a cat");
        assertTrue(value < 100 && value > 0);
    }
}
