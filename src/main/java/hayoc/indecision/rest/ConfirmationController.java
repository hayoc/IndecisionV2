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
@SuppressWarnings("SpringJavaAutowiringInspection")
@RestController
public class ConfirmationController {

    private Indecision indecision;

    @Autowired
    public ConfirmationController() {
        indecision = new Indecision();
    }


    @RequestMapping(value = "/confirm", method = RequestMethod.POST)
    public void greeting(@RequestBody RESTDecision decision) {
        indecision.updateUserDataWithDecision(decision.getOptions(), decision.getChosen(), decision.getUser());
    }
}
