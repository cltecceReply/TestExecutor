package com.reply.batch;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication

public class TestExecutorBatchApplication {

    public static void main(String[] args) {
        ApplicationContext ctx = SpringApplication.run(TestExecutorBatchApplication.class, args);
        SpringApplication.exit(ctx);
        System.exit(0);
    }
}
