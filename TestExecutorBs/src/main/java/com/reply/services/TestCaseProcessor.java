package com.reply.services;

import com.codahale.metrics.*;
import com.reply.io.dto.TERecord;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.management.ServiceNotFoundException;
import java.text.DecimalFormat;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Component
public class TestCaseProcessor {

    protected final static String OK_SUFFIX = "_OK";
    protected final static String KO_SUFFIX = "_KO";
    @Autowired
    IEndpointRetrievalService endpointRetrievalService;
    @Autowired
    IWsInvocator wsInvocator;
    @Autowired
    MetricRegistry metricRegistry;

    public TestCaseProcessorOut executeTestCase(TERecord record) throws ExecutionException, InterruptedException, ServiceNotFoundException {
        TestCaseProcessorOut out = new TestCaseProcessorOut();
        CompletableFuture<String> actualResult;
        CompletableFuture<String> expectedResult;
        boolean match;
        Pair<String, String> endpointTarget = endpointRetrievalService.retrieveEndpointsPerService(record.getServiceName());
        try (Timer.Context ctx = metricRegistry.timer(record.getServiceName() + "_ACT").time()) {
            actualResult = wsInvocator.invokeService(endpointTarget.getLeft(), record.getRequest());
        }
        try (Timer.Context ctx = metricRegistry.timer(record.getServiceName() + "_EXP").time()) {
            expectedResult = wsInvocator.invokeService(endpointTarget.getRight(), record.getRequest());
        }
        out.setActual(actualResult.get());
        out.setExpected(expectedResult.get());
        match = actualResult.get().equals(expectedResult.get());
        metricRegistry.counter(getRegisterName(record.getServiceName(), match)).inc();
        out.setPassed(match);
        return out;
    }

    protected String getRegisterName(String serviceName, boolean outType) {
        String suffix = outType ? OK_SUFFIX : KO_SUFFIX;
        return serviceName + suffix;
    }

    public String getReport() {
//        "Service, count, ok, ko, mean, min, max, median"
        String pattern = StringUtils.repeat("%13s", 12) + "%n";
        DecimalFormat df = new DecimalFormat("#.##");
        StringBuilder builder = new StringBuilder("%n");
        Map<String, Pair<String, String>> services = endpointRetrievalService.retrieveEndpointsPerService();
        builder.append(String.format(pattern, "SERVICE NAME", " COUNT", "OK", "KO",
                "MEAN ACT", "MIN ACT", "MAX ACT", "MEDIAN ACT",
                "MEAN EXP", "MIN EXP", "MAX EXP", "MEDIAN EXP"));
        for (Map.Entry<String, Pair<String, String>> entry : services.entrySet()) {
            Snapshot snapshotAct = metricRegistry.timer(entry.getValue().getLeft()).getSnapshot();
            Snapshot snapshotExp = metricRegistry.timer(entry.getValue().getRight()).getSnapshot();
            builder.append(String.format(pattern,
                    entry.getKey(),
                    metricRegistry.counter(entry.getKey()).getCount(),
                    metricRegistry.counter(getRegisterName(entry.getKey(), true)).getCount(),
                    metricRegistry.counter(getRegisterName(entry.getKey(), false)).getCount(),
                    df.format(snapshotAct.getMean()),
                    df.format(snapshotAct.getMin()),
                    df.format(snapshotAct.getMax()),
                    df.format(snapshotAct.getMedian()),
                    df.format(snapshotExp.getMean()),
                    df.format(snapshotExp.getMin()),
                    df.format(snapshotExp.getMax()),
                    df.format(snapshotExp.getMedian())
            ));
        }
        return builder.toString();
    }

}
