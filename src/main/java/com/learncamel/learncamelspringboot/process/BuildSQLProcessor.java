package com.learncamel.learncamelspringboot.process;

import com.learncamel.learncamelspringboot.domain.Item;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class BuildSQLProcessor implements Processor {

    @Override
    public void process(Exchange exchange) {

        Item item = (Item) exchange.getIn().getBody();
        log.info("Item in Processor is: " + item);

        StringBuilder query = new StringBuilder();
        if (item.getTransactionType().equals("ADD")) {
            query.append("INSERT INTO ITEMS (SKU, ITEM_DESCRIPTION, PRICE) VALUES ('" + item.getSku() + "','" + item
                    .getItemDescription() + "'," + item.getPrice() + ")");
        } else if (item.getTransactionType().equals("UPDATE")) {
        } else if (item.getTransactionType().equals("DELETE")) {}

        log.info("Final query is: " + query);

        exchange.getIn().setBody(query.toString());
    }

}
