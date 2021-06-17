//package com.reply.services.impl;
//
//
//import lombok.extern.slf4j.Slf4j;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.test.context.ContextConfiguration;
//import org.springframework.test.context.TestPropertySource;
//import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
//import org.springframework.test.context.support.AnnotationConfigContextLoader;
//
//import javax.management.ServiceNotFoundException;
//
//@Slf4j
//@RunWith(SpringJUnit4ClassRunner.class)
//@ContextConfiguration(loader = AnnotationConfigContextLoader.class)
//@TestPropertySource("classpath:application.properties")
//public class EndpointRetrievalServiceTest {
//
//
////    @Configuration
////    static class ContextConfiguration {
////        @Bean
////        EndpointRetrievalService service() {
////            return new EndpointRetrievalService();
////        }
////    }
////
////    @Autowired
////    EndpointRetrievalService service;
////
////    @Test
////    public void peropertyLoadingTest() throws ServiceNotFoundException {
////        log.info("Endpoints: {}", service.endpoints);
//////        Pair<String, String> endpoints = service.retrieveEndpointsPerService("TEST");
//////        log.info("Pair: {}", endpoints);
////    }
//}
