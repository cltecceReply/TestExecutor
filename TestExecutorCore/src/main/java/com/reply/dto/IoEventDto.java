package com.reply.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
//@JsonDeserialize(using=IoEventJsonDeserializer.class)
public class IoEventDto {

    @JsonProperty("table_name")
    String tableName;
    IoOperationDto operation;

    public IoEventDto(){
        operation = new IoOperationDto();
    }
}
