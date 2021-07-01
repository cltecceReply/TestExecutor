package com.reply.services.processor;

import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.Snapshot;
import com.reply.io.dto.TERecord;
import com.reply.io.model.DbOperation;
import com.reply.model.ComparisonOutcome;
import com.reply.model.EndpointServiceOut;
import com.reply.model.TestCaseProcessorOut;
import com.reply.services.comparator.XmlComparatorService;
import com.reply.services.endpoint.IEndpointRetrievalService;
import com.reply.services.invoker.IWebServiceInvoker;
import com.reply.services.writes.IIOProviderService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.management.ServiceNotFoundException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
public class TestCaseProcessor {

    protected static final String OK_SUFFIX = "_OK";
    protected static final String KO_SUFFIX = "_KO";
    private static final int REPORT_ITEMS = 12;

    @Value("${comparison.verbose:false}")
    boolean verbose;
    @Value("${comparison.ignoreSpaces:false}")
    boolean ignoreSpaces;
    @Value("${comparison.writesleep:5}")
    int writeSleep;

    @Autowired
    @Qualifier("DbEndpointRetrivalService")
    protected IEndpointRetrievalService endpointRetrievalService;
    @Autowired
    protected IWebServiceInvoker wsInvocator;
    @Autowired
    protected IIOProviderService ioProviderService;
    @Autowired(required = false)
    protected MetricRegistry metricRegistry;

    XmlComparatorService comparatorService;

    public TestCaseProcessor() {
        comparatorService = XmlComparatorService.builder()
                .ignoreSpaces(ignoreSpaces)
                .build();
    }

    @PostConstruct
    public void init() {
        log.info("Configuration:");
        log.info("write sleep time: {} THIS VALUE MUST MATCH THE POLLING RATE OF THE STORE NODE!!", writeSleep);
    }

    public TestCaseProcessorOut executeTestCase(TERecord teRecord) throws ExecutionException, InterruptedException, ServiceNotFoundException {
        TestCaseProcessorOut out = new TestCaseProcessorOut();
        CompletableFuture<String> actualResult;
        CompletableFuture<String> expectedResult;
        List<DbOperation> writesActual = new ArrayList<>();
        List<DbOperation> writesExpected = new ArrayList<>();
        boolean writeMatch = true;

        EndpointServiceOut endpointTarget = endpointRetrievalService.retrieveEndpointsPerService(teRecord.getServiceName());
        actualResult = wsInvocator.invokeService(endpointTarget.getActualEndpoint(), teRecord.getRequest());
        if (teRecord.isWrite()) {
            //Devo aspettare la fine dell'elaborazione per recuperare i dati sulle scritture
            actualResult.join();
            writesActual = this.retrieveWrites();
        }
        expectedResult = wsInvocator.invokeService(endpointTarget.getTargetEndpoint(), teRecord.getRequest());
        if (teRecord.isWrite()) {
            //Devo aspettare la fine dell'elaborazione per recuperare i dati sulle scritture
            actualResult.join();
            writesExpected = this.retrieveWrites();
        }

        out.setActual(actualResult.get());
        out.setExpected(expectedResult.get());
        ComparisonOutcome outcome = comparatorService.areEqual(out.getExpected(), out.getActual());
        if (verbose && !outcome.isMatch()) {
            log.info("[{}][{}] Test Failed with {} differences", teRecord.getServiceName(), teRecord.getTestId(), outcome.getDifferences().size());
            outcome.getDifferences().forEach(diff -> log.info("[{}][{}] {}", teRecord.getServiceName(), teRecord.getTestId(), diff));
        }
        if (teRecord.isWrite())
            writeMatch = this.compareWrites(teRecord, writesActual, writesExpected);
        out.setPassed(outcome.isMatch() && writeMatch);
        if (metricRegistry != null)
            metricRegistry.counter(getRegisterName(teRecord.getServiceName(), out.isPassed())).inc();
        return out;
    }

    private List<DbOperation> retrieveWrites() {
        List<DbOperation> ops;
        try {
            TimeUnit.SECONDS.sleep(writeSleep);
        } catch (InterruptedException ie) {
            log.error("Unable to wait");
            Thread.currentThread().interrupt();
        }
        return ioProviderService.getAndFlush();
    }

    private boolean compareWrites(TERecord teRecord, List<DbOperation> actual, List<DbOperation> expected) {
        if (actual.size() != expected.size())
            return false;
        boolean eq = true;
        int i = 0;
        Iterator<DbOperation> e = expected.iterator();
        Iterator<DbOperation> a = actual.iterator();
        DbOperation opA = null;
        DbOperation opE = null;
        while (e.hasNext() && a.hasNext() && eq) {
            opA = a.next();
            opE = e.next();
            eq = opA.equals(opE);
            i++;
        }
        if (!eq && verbose) {
            log.info("[{}][{}] Writes op n. {} do not match", teRecord.getServiceName(), teRecord.getTestId(), i);
            log.info("[{}][{}] Expected: |{}|", teRecord.getServiceName(), teRecord.getTestId(), opE);
            log.info("[{}][{}] Actual  : |{}|", teRecord.getServiceName(), teRecord.getTestId(), opA);
        }
        return eq;
    }

    protected String getRegisterName(String serviceName, boolean outType) {
        String suffix = outType ? OK_SUFFIX : KO_SUFFIX;
        return serviceName + suffix;
    }

    public String getReport() {
        if (this.metricRegistry == null)
            return "Report Not Available";
//        "Service, count, ok, ko, mean, min, max, median"
        String pattern = StringUtils.repeat("|%13s", REPORT_ITEMS) + "%n";
        StringBuilder builder = new StringBuilder("\n");
        Map<String, EndpointServiceOut> services = endpointRetrievalService.retrieveEndpointsPerService();
        builder.append(String.format(pattern, "SERVICE NAME", " COUNT", "OK", "KO",
                "MEAN ACT(ms)", "MIN ACT(ms)", "MAX ACT(ms)", "MEDIAN ACT(ms)",
                "MEAN EXP(ms)", "MIN EXP(ms)", "MAX EXP(ms)", "MEDIAN EXP(ms)"))
                .append("|").append(StringUtils.repeat("-", 14 * REPORT_ITEMS)).append("|\n");
        for (Map.Entry<String, EndpointServiceOut> entry : services.entrySet()) {
            Snapshot snapshotAct = metricRegistry.timer(entry.getValue().getActualEndpoint()).getSnapshot();
            Snapshot snapshotExp = metricRegistry.timer(entry.getValue().getTargetEndpoint()).getSnapshot();
            long testOk = metricRegistry.counter(getRegisterName(entry.getKey(), true)).getCount();
            long testKo = metricRegistry.counter(getRegisterName(entry.getKey(), false)).getCount();
            builder.append(String.format(pattern,
                    entry.getKey(),
                    testOk + testKo,
                    testOk,
                    testKo,
                    TimeUnit.MILLISECONDS.convert(new Double(snapshotAct.getMean()).longValue(), TimeUnit.NANOSECONDS),
                    TimeUnit.MILLISECONDS.convert(snapshotAct.getMin(), TimeUnit.NANOSECONDS),
                    TimeUnit.MILLISECONDS.convert(snapshotAct.getMax(), TimeUnit.NANOSECONDS),
                    TimeUnit.MILLISECONDS.convert(new Double(snapshotAct.getMedian()).longValue(), TimeUnit.NANOSECONDS),
                    TimeUnit.MILLISECONDS.convert(new Double(snapshotExp.getMean()).longValue(), TimeUnit.NANOSECONDS),
                    TimeUnit.MILLISECONDS.convert(snapshotExp.getMin(), TimeUnit.NANOSECONDS),
                    TimeUnit.MILLISECONDS.convert(snapshotExp.getMax(), TimeUnit.NANOSECONDS),
                    TimeUnit.MILLISECONDS.convert(new Double(snapshotExp.getMedian()).longValue(), TimeUnit.NANOSECONDS)
            ));
        }
        return builder.toString();
    }

}
