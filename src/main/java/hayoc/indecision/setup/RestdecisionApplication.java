package hayoc.indecision.setup;

import hayoc.indecision.rest.DecisionController;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackageClasses = {DecisionController.class, RestdecisionApplication.class})
public class RestdecisionApplication {

	public static void main(String[] args) {
		SpringApplication.run(RestdecisionApplication.class, args);
	}
}
