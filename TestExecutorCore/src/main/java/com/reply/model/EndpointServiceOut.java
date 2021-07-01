package com.reply.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class EndpointServiceOut {
    private String actualEndpoint;
    private String targetEndpoint;
}
