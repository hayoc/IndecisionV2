package hayoc.indecision.features.category;

import hayoc.indecision.features.Feature;
import hayoc.indecision.util.PropertyReader;
import org.apache.commons.codec.binary.StringUtils;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.IOException;
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

    private static final double DEFAULT = 50.0;
    private static final int MAX_PADDING = 4;
    private static final int MAX_NGRAMS = 5;
    private static final Pattern LETTERS = Pattern.compile("[a-zA-Z0-9]");
    private static final String WS = " ";
    private static final String US = "_";

    private static Map<String, Set<String>> MODEL_VALUES = new HashMap<>();

    @Override
    public double getValue(String user, String option) {
        Set<String> model = getNgramModel(pad(option));
        Map<String, Set<String>> values = getAllNgramModels();
        for (Map.Entry<String, Set<String>> entry : values.entrySet()) {
            if (!Collections.disjoint(entry.getValue(), model)) {
                return getUserValueForModel(user, entry.getKey());
            }
        }
        return DEFAULT;
    }

    public void update(List<String> options, String chosen, String user) {
        Map<String, Set<String>> values = getAllNgramModels();
        for (String option : options) {
            Set<String> model = getNgramModel(pad(option));
            for (Map.Entry<String, Set<String>> entry : values.entrySet()) {
                String key = entry.getKey();
                Set<String> value = entry.getValue();
                if (!Collections.disjoint(value, model)) {
                    writeAppend(model, value, user, key, StringUtils.equals(option, chosen));
                } else {
                    writeNew(model, values, user, key);
                }
            }
        }

        MODEL_VALUES = updateFiles(getUniqueNgramModels(values));
    }

    public void initialize(String user, String option, boolean chosen) {
        Set<String> model = getNgramModel(pad(option));
        Map<String, Set<String>> values = getAllNgramModels();
        writeNew(model, values, user, UUID.randomUUID().toString());
        MODEL_VALUES = getUniqueNgramModels(values);
    }

    public void finishInitialization() {
        MODEL_VALUES.entrySet().removeIf(entry -> entry.getValue().isEmpty());
    }

    private void writeAppend(Set<String> model, Set<String> value, String user, String key, boolean chosen) {
        try {
            Files.write(Paths.get(PATHS.getProperty("USER_CATEGORIES") + File.separator + "values" + File.separator + key + US + user), String.valueOf(updateUserValue(getUserValueForModel(user, key), chosen)).getBytes());
            value.addAll(model);
        } catch (IOException e) {
            LOG.error("Could not write NGRAM MODEL value for USER/MODEL: " + user + " " + key);
        }
    }

    private void writeNew(Set<String> model, Map<String, Set<String>> values, String user, String key) {
        try {
            Files.write(Paths.get(PATHS.getProperty("USER_CATEGORIES") + File.separator + "values" + File.separator + key + US + user), String.valueOf(DEFAULT).getBytes());
            values.put(key, model);
        } catch (IOException e) {
            LOG.error("Could not write NGRAM MODEL value for USER/MODEL: " + user + " " + key);
        }
    }

    private Map<String, Set<String>> updateFiles(Map<String, Set<String>> models) {
        for (Map.Entry<String, Set<String>> entry : models.entrySet()) {
            try {
                Files.write(Paths.get(PATHS.getProperty("USER_CATEGORIES") + File.separator + "models" + File.separator + entry.getKey()), entry.getValue(), StandardCharsets.UTF_8);
            } catch (IOException e) {
                LOG.error("Could not write NGRAM MODEL for MODEL: " + entry.getKey());
            }
        }

        return models;
    }

    private Map<String, Set<String>> getUniqueNgramModels(Map<String, Set<String>> ngramModels) {
        Set<String> intersection = new HashSet<>();
        List<Set<String>> ngramLists = new ArrayList<>(ngramModels.values());

        for (int i = 0; i < ngramLists.size(); i++) {
            for (String ngram : ngramLists.get(i)) {
                if (i + 1 >= ngramLists.size())
                    break;
                if (ngramLists.get(i + 1).contains(ngram))
                    intersection.add(ngram);
            }
        }

        for (Set<String> ngrams : ngramModels.values()) {
            ngrams.removeAll(intersection);
        }

        return ngramModels;
    }

    private Map<String, Set<String>> getAllNgramModels() {
        if (!MODEL_VALUES.isEmpty()) return MODEL_VALUES;
        File directory = new File(PATHS.getProperty("USER_CATEGORIES") + File.separator + "models");
        File[] files = directory.listFiles();
        if (files != null) {
            for (File file : files) {
                try {
                    MODEL_VALUES.put(file.getName(), new HashSet<>(Files.readAllLines(Paths.get(file.getAbsolutePath()))));
                } catch (IOException e) {
                    LOG.error("Could not read NGRAM MODEL from File Archive.");
                }
            }
        }
        return MODEL_VALUES;
    }

    private double getUserValueForModel(String user, String key) {
        try {
            List<String> lines = Files.readAllLines(Paths.get(PATHS.getProperty("USER_CATEGORIES") + File.separator + "values" + File.separator + key + US + user));
            return lines.size() > 0 ? Double.valueOf(lines.get(0)) : DEFAULT;
        } catch (IOException e) {
            LOG.error("Could not read NGRAM MODEL value for user & model: " + user + " - " + key);
            return DEFAULT;
        }
    }

    private double updateUserValue(double value, boolean chosen) {
        if (chosen && value < 100) {
            return value + 1;
        } else if (value > 0) {
            return value - 1;
        } else {
            return value;
        }
    }

    private Set<String> getNgramModel(String sentence) {
        Set<String> ngramModel = new HashSet<>();
        for (int i = 1; i <= MAX_NGRAMS; i++) {
            ngramModel.addAll(getNgrams(sentence, i));
        }
        if (ngramModel.isEmpty()) {
            LOG.warn("Empty ngram model for: " + sentence);
        }
        return ngramModel;
    }

    private Set<String> getNgrams(String sentence, int ngramSize) {
        Set<String> ngramList = new HashSet<>();
        String[] tokens = sentence.split("[\\W]+");
        for (int pos = 0; pos < tokens.length; pos++) {
            StringBuilder ngram = new StringBuilder();
            for (int i = 0; i < ngramSize; i++) {
                if (pos + i >= tokens.length) break;
                ngram.append(tokens[pos + i]).append(WS);
            }
            if (LETTERS.matcher(ngram.toString()).find())
                ngramList.add(ngram.toString().toLowerCase());
        }
        return ngramList;
    }

    private String pad(String sentence) {
        StringBuilder builder = new StringBuilder(sentence);
        builder.reverse();
        for (int i = 0; i < MAX_PADDING; i++) {
            builder.append(" _");
        }
        builder.reverse();
        for (int i = 0; i < MAX_PADDING; i++) {
            builder.append(" _");
        }
        return builder.toString();
    }
}
