package com.learncamel.learncamelspringboot.process;

import com.learncamel.learncamelspringboot.domain.Item;
import com.learncamel.learncamelspringboot.exception.DataException;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

@Component
@Slf4j
public class BuildSQLProcessor implements Processor {

    @Override
    public void process(Exchange exchange) {

        Item item = (Item) exchange.getIn().getBody();
        log.info("Item in Processor is: " + item);
        String tableName = "ITEMS1";
        
        if (ObjectUtils.isEmpty(item.getSku())) {
            throw new DataException("Sku is null " + item.getItemDescription());
        }

        StringBuilder query = new StringBuilder();
        if (item.getTransactionType().equals("ADD")) {
            query.append("INSERT INTO " + tableName + " (SKU, ITEM_DESCRIPTION, PRICE) VALUES ('" + item.getSku() + "','" + item
                    .getItemDescription() + "'," + item.getPrice() + ")");
        } else if (item.getTransactionType().equals("UPDATE")) {
            query.append("UPDATE " + tableName + " SET PRICE = " + item.getPrice());
            query.append(" WHERE SKU = '" + item.getSku() + "'");
        } else if (item.getTransactionType().equals("DELETE")) {
            query.append("DELETE FROM " + tableName + " WHERE SKU = '" + item.getSku() + "'");
        }

        log.info("Final query is: " + query);

        exchange.getIn().setBody(query.toString());
    }

}
