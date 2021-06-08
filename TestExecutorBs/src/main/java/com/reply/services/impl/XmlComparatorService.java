package com.reply.services.impl;

import com.reply.services.ComparisonOutcome;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;
import org.xmlunit.builder.DiffBuilder;
import org.xmlunit.diff.Diff;
import org.xmlunit.diff.Difference;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

@Slf4j
public class XmlComparatorService {

    boolean ignoreSpaces;

    public XmlComparatorService() {
        this.ignoreSpaces = false;
    }

    @Builder
    public XmlComparatorService(boolean ignoreSpaces) {
        this.ignoreSpaces = ignoreSpaces;
    }

    public ComparisonOutcome areEqual(String target, String actual) {
        Diff diff = this.buildComparator(target, actual);
        ComparisonOutcome compareOutput = new ComparisonOutcome();
        List<String> differences = new LinkedList<>();
        compareOutput.setMatch(!diff.hasDifferences());
        if (diff.hasDifferences()) {
            Iterator<Difference> iter = diff.getDifferences().iterator();
            int size = 0;
            while (iter.hasNext()) {
                differences.add(iter.next().toString());
                size++;
            }
            compareOutput.setDifferences(differences);
            if (log.isDebugEnabled()) {
                log.debug("Differences found: {}", size);
                log.debug("Differences\n{}", String.join("\n", differences));
            }
        }

        return compareOutput;
    }


    protected Diff buildComparator(String target, String actual) {
        DiffBuilder diffBuilder = DiffBuilder.compare(target)
                .withTest(actual)
                .ignoreComments();

        if (ignoreSpaces)
            diffBuilder.ignoreWhitespace();

        return diffBuilder.build();
    }

    public boolean isIgnoreSpaces() {
        return ignoreSpaces;
    }

    public void setIgnoreSpaces(boolean ignoreSpaces) {
        this.ignoreSpaces = ignoreSpaces;
    }
}
