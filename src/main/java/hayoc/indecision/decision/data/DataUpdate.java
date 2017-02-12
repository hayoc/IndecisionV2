package hayoc.indecision.decision.data;

/**
 * Created by Hayo on 12/02/2017.
 */
public interface DataUpdate {

    void append(String user, double[] features, boolean result);
}
