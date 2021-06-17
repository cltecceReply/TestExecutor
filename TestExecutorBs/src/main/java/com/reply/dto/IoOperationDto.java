package com.reply.dto;

import com.reply.io.dto.EventTypeEnum;
import lombok.Data;

import java.io.Serializable;
import java.util.Map;

@Data
public class IoOperationDto implements Serializable {


    EventTypeEnum eventType;
    Map<String, String> data;
}

