package com.learncamel.learncamelspringboot.route;

import lombok.extern.slf4j.Slf4j;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class SimpleCamelRoute extends RouteBuilder {

    private final Environment environment;

    public SimpleCamelRoute(Environment environment) {
        this.environment = environment;
    }

    public void configure() {

        log.info("Starting the camel route");

        from("{{startRoute}}")
                .log("Timer Invoked and the body " + environment.getProperty("message"))
                .choice()
                    // Really don't like this. We shouldn't have to design the code around the test
                    .when((header("env").isNotEqualTo("mock")))
                        .pollEnrich("{{fromRoute}}")
                    .otherwise()
                        .log("mock env flow and the body is ${body}")
                    .end()
                .to("{{toRoute1}}");

        log.info("Ending the camel route");
    }
}
