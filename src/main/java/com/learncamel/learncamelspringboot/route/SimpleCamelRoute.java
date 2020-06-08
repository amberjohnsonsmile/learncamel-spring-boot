package com.learncamel.learncamelspringboot.route;

import com.learncamel.learncamelspringboot.config.DbConfig;
import com.learncamel.learncamelspringboot.domain.Item;
import com.learncamel.learncamelspringboot.process.BuildSQLProcessor;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.dataformat.bindy.csv.BindyCsvDataFormat;
import org.apache.camel.spi.DataFormat;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class SimpleCamelRoute extends RouteBuilder {

    @Qualifier("dataSource")
    private final DbConfig dbConfig;
    private final Environment environment;
    private final BuildSQLProcessor buildSQLProcessor;
    private final SuccessProcessor successProcessor;

    public SimpleCamelRoute(Environment environment) {
        this.environment = environment;
        this.dbConfig = new DbConfig();
        this.buildSQLProcessor = new BuildSQLProcessor();
        this.successProcessor = new SuccessProcessor();
    }

    public void configure() {

        log.info("Starting the camel route");

        DataFormat bindy = new BindyCsvDataFormat(Item.class);

        from("{{startRoute}}")
                .log("Timer Invoked and the body " + environment.getProperty("message"))
                .choice()
                    // Really don't like this. We shouldn't have to design the code around the test
                    .when((header("env").isNotEqualTo("mock")))
                        .pollEnrich("{{fromRoute}}")
                    .otherwise()
                        .log("mock env flow and the body is ${body}")
                    .end()
                .to("{{toRoute1}}")
                .unmarshal(bindy)
                .log("The unmarshalled object is ${body}")
                .split(body())
                    .log("Record is ${body}")
                    .process(buildSQLProcessor)
                    .to("{{toRoute2}}")
                .end()
            // Will only reach this if the route is successful
            .process(successProcessor)
            .to("{{toRoute3}}");

        log.info("Ending the camel route");
    }
}
