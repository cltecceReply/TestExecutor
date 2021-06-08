package com.reply.services.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.xmlunit.builder.DiffBuilder;
import org.xmlunit.diff.Diff;
import org.xmlunit.diff.Difference;

import java.util.Iterator;

@Slf4j
@Service
public class XmlComparatorService {

    @Value("${comparison.verbose:false}")
    boolean verbose;
    @Value("${comparison.ignoreSpaces:false}")
    boolean ignoreSpaces;

    public boolean areEqual(String target, String actual){
        StringBuilder sb = new StringBuilder();
        DiffBuilder diffBuilder = DiffBuilder.compare(target)
                .withTest(actual)
                .ignoreComments();

        if(ignoreSpaces)
            diffBuilder.ignoreWhitespace();

        Diff diff = diffBuilder.build();
        if(verbose && diff.hasDifferences()) {
            Iterator<Difference> iter = diff.getDifferences().iterator();
            int size = 0;
            while (iter.hasNext()) {
                sb.append(iter.next().toString())
                        .append("\n");
                size++;
            }
            log.info("Differences found: {}", size);
            log.info("Differences\n{}", sb);
        }
        return !diff.hasDifferences();
    }

    public boolean isVerbose() {
        return verbose;
    }

    public void setVerbose(boolean verbose) {
        this.verbose = verbose;
    }

    public boolean isIgnoreSpaces() {
        return ignoreSpaces;
    }

    public void setIgnoreSpaces(boolean ignoreSpaces) {
        this.ignoreSpaces = ignoreSpaces;
    }
}
