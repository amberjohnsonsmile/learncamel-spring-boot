package com.learncamel.learncamelspringboot.route;

import org.apache.camel.CamelContext;
import org.apache.camel.EndpointInject;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.apache.camel.test.spring.CamelSpringBootRunner;
import org.apache.camel.test.spring.MockEndpoints;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("mock")
@RunWith(CamelSpringBootRunner.class)
@SpringBootTest
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
@MockEndpoints
public class SimpleCamelRouteMockTest {
//    public class SimpleCamelRouteMockTest extends CamelTestSupport {

    @Autowired
    CamelContext camelContext;
    @Autowired
    Environment environment;
    @Autowired
    ProducerTemplate producerTemplate;

    @EndpointInject("mock:output")
    private MockEndpoint mockEndpoint;

    @EndpointInject("mock:output2")
    private MockEndpoint mockEndpoint1;

    // Override this method
    @Autowired
    protected CamelContext createCamelContext() {
        return camelContext;
    }

    @Test
    public void testMoveFileMock() throws InterruptedException {
        String message = "type,sku#,itemdescription,price\n"
                + "ADD,100,Samsung TV,500\n"
                + "ADD,101,LG TV,500";

        mockEndpoint.expectedMessageCount(1);
        mockEndpoint.expectedBodiesReceived(message);

        producerTemplate.sendBodyAndHeader(environment.getProperty("startRoute"), message, "env",
                environment.getProperty("spring.profiles.active"));

        mockEndpoint.assertIsSatisfied();
    }

    @Test
    public void testMoveFileMockAndDb() throws InterruptedException {
        String message = "type,sku#,itemdescription,price\n"
                + "ADD,100,Samsung TV,500\n"
                + "ADD,101,LG TV,500";

        String outMessage = "Data updated successfully";

        mockEndpoint.expectedMessageCount(1);
        mockEndpoint.expectedBodiesReceived(message);
        mockEndpoint1.expectedMessageCount(1);
        mockEndpoint1.expectedBodiesReceived(outMessage);


        producerTemplate.sendBodyAndHeader(environment.getProperty("startRoute"), message, "env",
                environment.getProperty("spring.profiles.active"));

        mockEndpoint.assertIsSatisfied();
    }

}
