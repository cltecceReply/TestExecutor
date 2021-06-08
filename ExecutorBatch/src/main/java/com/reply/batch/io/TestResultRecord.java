package com.reply.batch.io;

import lombok.Data;

@Data
public class TestResultRecord {
    private long id;
    private String serviceName;
    private String expected;
    private String actual;
    private boolean passed;
}
