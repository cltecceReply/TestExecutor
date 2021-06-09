package com.reply.services.impl;

import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.Timer;
import com.reply.services.IWsInvocator;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.InputStreamEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.CompletableFuture;

@Slf4j
@Service
public class WsInvocator implements IWsInvocator, InitializingBean {

    @Value("${httpclient.sockettimeout:3}")
    protected int socketTimeout;
    @Value("${httpclient.readtimeout:3}")
    protected int readTimeout;

    @Autowired
    MetricRegistry metricRegistry;

    RequestConfig requestConfig;

    @Override
    public void afterPropertiesSet() throws Exception {
        log.info("Socket Timeout: {}", socketTimeout);
        log.info("ReadTimeout Timeout: {}", readTimeout);
        requestConfig = RequestConfig.custom()
                .setSocketTimeout(socketTimeout * 1000)
                .setConnectTimeout(readTimeout * 1000)
                .build();
    }

    @Async
    @Override
    public CompletableFuture<String> invokeService(String endpoint, String xmlSoapRequest) {
        // SOAP request(xml) read-in
        CompletableFuture<String> resFut = new CompletableFuture<>();
        String res = "";
        // SOAP request send
        //TODO: opt, client thread local
        try (CloseableHttpClient httpclient = HttpClients.custom().setDefaultRequestConfig(requestConfig)
                .build()) {

            HttpPost post = new HttpPost(endpoint);
            post.setEntity(new InputStreamEntity(new ByteArrayInputStream(xmlSoapRequest.getBytes(StandardCharsets.UTF_8))));
            post.setHeader("Content-type", "text/xml; charset=UTF-8");
            post.setHeader("SOAPAction", "");
            try (Timer.Context ctx = metricRegistry.timer(endpoint).time();
                 CloseableHttpResponse response = httpclient.execute(post)) {
                HttpEntity entity = response.getEntity();
                res = EntityUtils.toString(entity);
                EntityUtils.consume(entity);
                resFut.complete(res);
            }
        } catch (Exception e) {
            log.warn("Invokation failed: ", e);
            resFut.complete(e.getMessage());
        }
        return resFut;
    }

}
