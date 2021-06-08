package com.reply.services;

import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.Snapshot;
import com.reply.io.dto.TERecord;
import com.reply.services.impl.XmlComparatorService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.management.ServiceNotFoundException;
import java.text.DecimalFormat;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Slf4j
@Component
public class TestCaseProcessor {

    protected final static String OK_SUFFIX = "_OK";
    protected final static String KO_SUFFIX = "_KO";

    @Value("${comparison.verbose:false}")
    boolean verbose;
    @Value("${comparison.ignoreSpaces:false}")
    boolean ignoreSpaces;

    @Autowired
    IEndpointRetrievalService endpointRetrievalService;
    @Autowired
    IWsInvocator wsInvocator;

    @Autowired
    MetricRegistry metricRegistry;

    XmlComparatorService comparatorService;

    public TestCaseProcessor(){
        comparatorService =  XmlComparatorService.builder()
                .ignoreSpaces(ignoreSpaces)
                .build();
    }

    public TestCaseProcessorOut executeTestCase(TERecord teRecord) throws ExecutionException, InterruptedException, ServiceNotFoundException {
        TestCaseProcessorOut out = new TestCaseProcessorOut();
        CompletableFuture<String> actualResult;
        CompletableFuture<String> expectedResult;
        Pair<String, String> endpointTarget = endpointRetrievalService.retrieveEndpointsPerService(teRecord.getServiceName());
        actualResult = wsInvocator.invokeService(endpointTarget.getLeft(), teRecord.getRequest());
        expectedResult = wsInvocator.invokeService(endpointTarget.getRight(), teRecord.getRequest());
        out.setActual(actualResult.get());
        out.setExpected(expectedResult.get());
        ComparisonOutcome outcome = comparatorService.areEqual(out.getExpected(), out.getActual());
        if (verbose && !outcome.isMatch()) {
            log.info("[{}][{}] Test Failed", teRecord.getServiceName(), teRecord.getTestId());
            outcome.getDifferences().forEach(diff -> log.info("[{}][{}] {}", teRecord.getServiceName(), teRecord.getTestId(), diff));
        }
        out.setPassed(outcome.isMatch());
        metricRegistry.counter(getRegisterName(teRecord.getServiceName(), out.isPassed())).inc();
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
