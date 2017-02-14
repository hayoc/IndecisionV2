package hayoc.indecision.features.category;

import hayoc.indecision.features.Feature;
import hayoc.indecision.util.PropertyReader;
import org.apache.commons.codec.binary.StringUtils;
import org.apache.log4j.Logger;
import weka.clusterers.SimpleKMeans;
import weka.core.Instances;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.StringToWordVector;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Stream;

/**
 * Created by Hayo on 12/02/2017.
 */
public class CategoryFeature implements Feature {

    private static final Logger LOG = Logger.getLogger(CategoryFeature.class);
    private static final PropertyReader PATHS = new PropertyReader("application.properties");

    @Override
    public double getValue(String user, String option) {
        try {
            String path = PATHS.getProperty("USER_CATEGORIES") + File.separator + user;
            List<String> lines = Files.readAllLines(Paths.get(path));
            lines.add(option);

            int[] assignments = kMeans(lines.size(), prepareData(lines));
            int cluster = assignments[assignments.length - 1];
            int trues = 0;
            for (int i = 0; i < assignments.length - 1; i++) {
                if (cluster == assignments[i]) {
                    if (Boolean.valueOf(lines.get(i).split("#")[1])) {
                        trues++;
                    }
                }
            }
            return trues/assignments.length - 1;
        } catch (Exception e) {
            LOG.error("Failed to cluster for user: " + user);
            e.printStackTrace();
            return 0.0;
        }
    }

    public void update(String user, String option) {
        try {
            Files.write(Paths.get(PATHS.getProperty("USER_CATEGORIES") + File.separator + user), option.getBytes(), StandardOpenOption.APPEND);
        } catch (IOException e) {
            LOG.error("Failed to update for user: " + user);
        }
    }

    private int[] kMeans(int numOptions, String balls) throws Exception {
        BufferedReader reader = new BufferedReader(new StringReader(balls));
        Instances unfiltered = new Instances(reader);

        StringToWordVector filter = new StringToWordVector();
        filter.setInputFormat(unfiltered);

        Instances filtered = Filter.useFilter(unfiltered, filter);

        SimpleKMeans kMeans = new SimpleKMeans();
        int k = (int) Math.ceil(Math.pow((numOptions/2), 0.5));
        k = k == 0 ? 1 : k;
        kMeans.setPreserveInstancesOrder(true);
        kMeans.setNumClusters(k);
        kMeans.buildClusterer(filtered);
        return kMeans.getAssignments();
    }

    private String prepareData(List<String> lines) throws IOException {
        StringBuilder sb = new StringBuilder();
        sb.append("@RELATION category\n");
        sb.append("@ATTRIBUTE text string\n");
        sb.append("@DATA\n");
        for (String line : lines) {
            sb.append("'").append(line.split("#")[0].trim()).append("'\n");
        }
        return sb.toString();
    }
}
