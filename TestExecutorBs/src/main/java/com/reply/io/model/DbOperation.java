package com.reply.io.model;

import com.reply.io.dto.EventTypeEnum;
import com.reply.io.dto.IoEventDto;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@NoArgsConstructor
public class DbOperation {
    String tableName;
    EventTypeEnum eventType;
    Map<String, String> data;
    Long id;

    public DbOperation(IoEventDto eventDto){
        this.tableName = eventDto.getTableName();
        this.eventType = eventDto.getOperation().getEventType();
        this.data = eventDto.getOperation().getData();
    }

    public DbOperation setIdAndReturn(Long id){
        this.id = id;
        return this;
    }
}
