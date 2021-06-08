package com.reply.io.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TERecord implements Serializable {
    private long testId;
    private String serviceName;
    private String request;
}
