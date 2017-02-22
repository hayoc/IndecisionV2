package hayoc.indecision.rest;

import hayoc.indecision.rest.entity.RESTDecision;
import hayoc.indecision.setup.Indecision;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by Hayo on 19/02/2017.
 */
@RestController
public class DecisionController {

    private Indecision indecision;

    @Autowired
    public DecisionController() {
        indecision = new Indecision();
    }

    @RequestMapping(value = "/newuser", method = RequestMethod.POST)
    public void newUser(@RequestBody RESTDecision decision) {
        indecision.createUser(decision.getUser());
    }

    @RequestMapping(value = "/resetuser", method = RequestMethod.POST)
    public void resetUser(@RequestBody RESTDecision decision) {
        indecision.createUser(decision.getUser());
    }


    @RequestMapping(value = "/decide", method = RequestMethod.POST)
    public RESTDecision decide(@RequestBody RESTDecision decision) {
        decision.setChosen(indecision.decide(decision.getOptions(), decision.getUser()));
        return decision;
    }
}
