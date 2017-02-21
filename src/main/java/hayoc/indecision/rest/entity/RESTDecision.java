package hayoc.indecision.rest.entity;

import java.util.List;

/**
 * Created by Hayo on 19/02/2017.
 */
public class RESTDecision {

    private String user;
    private List<String> options;
    private String chosen;

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public List<String> getOptions() {
        return options;
    }

    public void setOptions(List<String> options) {
        this.options = options;
    }

    public String getChosen() {
        return chosen;
    }

    public void setChosen(String chosen) {
        this.chosen = chosen;
    }
}
