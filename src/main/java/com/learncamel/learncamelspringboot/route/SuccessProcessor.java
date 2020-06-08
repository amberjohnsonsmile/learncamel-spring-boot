package com.learncamel.learncamelspringboot.route;

import org.apache.camel.Exchange;

public class SuccessProcessor implements org.apache.camel.Processor {
    @Override
    public void process(Exchange exchange) {
        exchange.getIn().setBody("Data updated successfully");
    }
}
