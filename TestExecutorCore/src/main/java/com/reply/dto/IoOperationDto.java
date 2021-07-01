package com.reply.dto;

import com.reply.io.dto.EventTypeEnum;
import lombok.Data;

import java.io.Serializable;
import java.util.Map;

@Data
public class IoOperationDto implements Serializable {


    private EventTypeEnum eventType;
    private Map<String, String> data;
}

