package com.reply.io.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.Map;

@Data
public class IoOperationDto implements Serializable {


    EventTypeEnum eventType;
    Map<String, String> data;
}

