package com.learncamel.learncamelspringboot.route;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import org.apache.camel.Exchange;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.test.spring.CamelSpringBootRunner;
import org.apache.commons.io.FileUtils;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("development")
@RunWith(CamelSpringBootRunner.class)
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
@SpringBootTest
public class SimpleCamelRouteTest {

    @Autowired
    ProducerTemplate producerTemplate;
    @Autowired
    Environment environment;

    @AfterClass
    public static void startCleanUp() throws IOException {
        FileUtils.cleanDirectory(new File("data/input"));
        FileUtils.deleteDirectory(new File("data/output"));
    }

    @Test
    public void testMoveFile() throws InterruptedException {
        String message = "type,sku#,itemdescription,price\n"
                + "ADD,100,Samsung TV,500\n"
                + "ADD,101,LG TV,500";
        String fileName = "fileTest.txt";

        // This starts the route
        producerTemplate.sendBodyAndHeader(environment.getProperty("fromRoute"), message, Exchange.FILE_NAME, fileName);

        // Wait for the file to be moved
        Thread.sleep(3000);

        // Assert it was moved successfully
        File outFile = new File("data/output/" + fileName);
        assertTrue(outFile.exists());
    }

    @Test
    public void testMoveFile_ADD() throws InterruptedException, IOException {
        String message = "type,sku#,itemdescription,price\n"
                + "ADD,100,Samsung TV,500\n"
                + "ADD,101,LG TV,500";
        String fileName = "fileTest.txt";

        // This starts the route
        producerTemplate.sendBodyAndHeader(environment.getProperty("fromRoute"), message, Exchange.FILE_NAME, fileName);

        // Wait for the file to be moved
        Thread.sleep(3000);

        // Assert it was moved successfully
        File outFile = new File("data/output/" + fileName);
        assertTrue(outFile.exists());

        // Assert the success processor created the file
        String outputMessage = "Data updated successfully";
        String output = new String(Files.readAllBytes(Paths.get("data/output/success.txt")));
        assertEquals(outputMessage, output);
    }

    // This test is failing, need to figure out why!
    @Test
    public void testMoveFile_UPDATE() throws InterruptedException, IOException {
        String message = "type,sku#,itemdescription,price\n"
                + "UPDATE,100,Samsung TV,600";
        String fileName = "fileUpdate.txt";

        // This starts the route
        producerTemplate.sendBodyAndHeader(environment.getProperty("fromRoute"), message, Exchange.FILE_NAME, fileName);

        // Wait for the file to be moved
        Thread.sleep(3000);

        // Assert it was moved successfully
        File outFile = new File("data/output/" + fileName);
        assertTrue(outFile.exists());

        // Assert the success processor created the file
        String outputMessage = "Data updated successfully";
        String output = new String(Files.readAllBytes(Paths.get("data/output/success.txt")));
        assertEquals(outputMessage, output);
    }

}
