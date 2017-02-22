package hayoc.indecision.features;

import org.junit.Test;

import static org.junit.Assert.assertFalse;

/**
 * Created by Hayo on 22/02/2017.
 */
public class FeaturesTest {

    @Test
    public void testFeaturesList() {
        assertFalse(Features.ALL.isEmpty());
    }
}
