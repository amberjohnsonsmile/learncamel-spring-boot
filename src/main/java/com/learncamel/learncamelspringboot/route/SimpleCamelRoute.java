package com.learncamel.learncamelspringboot.route;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
public class SimpleCamelRoute extends RouteBuilder {

    private final Environment environment;

    public SimpleCamelRoute(Environment environment) {
        this.environment = environment;
    }

    public void configure() {

        from("{{startRoute}}")
                .log("Timer Invoked and the body " + environment.getProperty("message"))
                .pollEnrich("{{fromRoute}}")
                .to("{{toRoute1}}");
    }
}
