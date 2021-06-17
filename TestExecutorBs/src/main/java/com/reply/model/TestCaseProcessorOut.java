package com.reply.model;

import lombok.Data;

@Data
public class TestCaseProcessorOut {
    private boolean passed;
    private String expected;
    private String actual;
}
