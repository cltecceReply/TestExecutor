package com.reply.kafka.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.reply.kafka.jsondeserializer.IoEventJsonDeserializer;
import lombok.Data;

@Data
@JsonDeserialize(using=IoEventJsonDeserializer.class)
public class IoEventDto {

    String tableName;
    IoOperationDto operation;

    public IoEventDto(){
        operation = new IoOperationDto();
    }
}
