package com.reply.services;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ComparisonOutcome {
    boolean match;
    List<String> differences = new ArrayList<>();
}
