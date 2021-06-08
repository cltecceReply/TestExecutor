package com.reply.services;

import lombok.Data;

@Data
public class TestCaseProcessorOut {
    private boolean passed;
    private String expected;
    private String actual;
}
