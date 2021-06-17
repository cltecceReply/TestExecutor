package com.reply.services.impl;

import com.reply.services.invoker.WebServiceInvoker;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.test.context.TestPropertySource;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Slf4j
@TestPropertySource("classpath:application.properties")
public class WebServiceInvokerTest extends WebServiceInvoker {

//    @Test
    public void testInvokeService() throws ExecutionException, InterruptedException {
        String msg = "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:gs=\"http://spring.io/guides/gs-producing-web-service\"> <soapenv:Header/> <soapenv:Body> <gs:getCountryRequest> <gs:name>Spain</gs:name> </gs:getCountryRequest> </soapenv:Body> </soapenv:Envelope>";
        this.readTimeout = 3;
        this.socketTimeout = 3;
        CompletableFuture<String> res = this.invokeService("http://localhost:8080/ws", msg);
        log.info("RESULT:");
        log.info(res.get());
        log.info(StringUtils.repeat("-", 100));
    }
}
