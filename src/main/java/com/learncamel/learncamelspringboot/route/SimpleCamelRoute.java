package com.learncamel.learncamelspringboot.route;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

@Component
public class SimpleCamelRoute extends RouteBuilder {

    public void configure() {

        from("{{startRoute}}")
                .log("Timer Invoked and the body is ${body}")
                .pollEnrich("{{fromRoute}}")
                .to("{{toRoute1}}");
    }
}
