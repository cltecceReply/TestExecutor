package com.reply.io.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.reply.io.jsondeserializer.IoEventJsonDeserializer;
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
