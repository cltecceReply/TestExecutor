//package com.reply.services.impl;
//
//import com.reply.services.IEndpointRetrievalService;
//import com.reply.services.IWsInvocator;
//import com.reply.services.TestCaseProcessor;
//import com.reply.services.TestCaseProcessorOut;
//import com.reply.io.dto.TERecord;
//import lombok.extern.slf4j.Slf4j;
//import org.apache.commons.lang3.tuple.ImmutablePair;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.TestConfiguration;
//import org.springframework.context.annotation.Bean;
//import org.springframework.scheduling.annotation.EnableAsync;
//import org.springframework.test.context.ContextConfiguration;
//import org.springframework.test.context.TestPropertySource;
//import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
//import org.springframework.test.context.support.AnnotationConfigContextLoader;
//
//import javax.management.ServiceNotFoundException;
//import java.util.concurrent.ExecutionException;
//
//import static org.mockito.Mockito.*;
//
//@Slf4j
//@RunWith(SpringJUnit4ClassRunner.class)
//@EnableAsync
//@ContextConfiguration(loader = AnnotationConfigContextLoader.class)
//@TestPropertySource("classpath:application.properties")
//public class TestCaseProcessorTest {
//
//    private static String msg = "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:gs=\"http://spring.io/guides/gs-producing-web-service\"> <soapenv:Header/> <soapenv:Body> <gs:getCountryRequest> <gs:name>Spain</gs:name> </gs:getCountryRequest> </soapenv:Body> </soapenv:Envelope>";
//    private static String endpointActual = "http://localhost:8080/ws";
//    @TestConfiguration
//    static class ContextConfiguration {
//
//        @Bean
//        IWsInvocator wsInvokator() {
//            return new WsInvocator();
//        }
//
//        @Bean
//        IEndpointRetrievalService endpointService() {
//            IEndpointRetrievalService service = mock(EndpointRetrievalService.class);
//            try {
//                when(service.retrieveEndpointsPerService(anyString()))
//                        .thenReturn(new ImmutablePair<>(endpointActual, endpointActual));
//            } catch (ServiceNotFoundException e) {
//                e.printStackTrace();
//            }
//            return service;
//        }
//
//        @Bean
//        TestCaseProcessor testCaseProcessor(){
//            return new TestCaseProcessor();
//        }
//    }
//
//    @Autowired
//    TestCaseProcessor testCaseProcessor;
//
//    @Test
//    public void testCaseProcessorTest() throws ExecutionException, InterruptedException, ServiceNotFoundException {
//        TERecord record = new TERecord();
//        record.setTestId(0);
//        record.setServiceName("TEST");
//        record.setRequest(msg);
//        TestCaseProcessorOut out = testCaseProcessor.executeTestCase(record);
//        log.info("Result: {}", out);
//    }
//
//}
