package hayoc.indecision.features.category;

import hayoc.indecision.util.MockPropertiesReader;
import org.junit.Before;
import org.junit.Test;

import java.io.File;

import static junit.framework.TestCase.assertTrue;

/**
 * Created by Hayo on 22/02/2017.
 */
public class CategoryFeatureTest {

    private MockPropertiesReader reader = new MockPropertiesReader();

    @Before
    public void setup() {
        File file = new File(getClass().getClassLoader().getResource("user_data/features/categorization/testuser").getFile());
        reader.setProperty("USER_CATEGORIES", file.getParent());
        CategoryFeature.setPATHS(reader);
    }

    @Test
    public void testCategoryFeature() {
        double value = new CategoryFeature().getValue("testuser", "should I get a cat");
        assertTrue(value < 100 && value > 0);
    }
}
