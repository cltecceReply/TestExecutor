package com.reply.services.impl;

import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class XmlComparatorTest {

    @Test
    public void compareTest() throws URISyntaxException {
        XmlComparatorService service = new XmlComparatorService();
        Path cblPath = Paths.get(getClass().getClassLoader()
                .getResource("responses/cobol_response.xml").toURI());
        Path javaPath = Paths.get(getClass().getClassLoader()
                .getResource("responses/java_response.xml").toURI());

        try(Stream<String> cblLines = Files.lines(cblPath);
            Stream<String> javaLines = Files.lines(javaPath)) {
            String cblData = cblLines.collect(Collectors.joining("\n"));
            String javaData = javaLines.collect(Collectors.joining("\n"));
            service.setIgnoreSpaces(false);
            boolean result = service.areEqual(cblData, javaData).isMatch();
            Assert.assertFalse(result);
            service.setIgnoreSpaces(true);
            result = service.areEqual(cblData, javaData).isMatch();
            Assert.assertTrue(result);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
