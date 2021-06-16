package com.reply.io.model;

import com.reply.io.dto.EventTypeEnum;
import com.reply.kafka.dto.IoEventDto;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;

import java.util.Comparator;
import java.util.Map;

import static java.util.Comparator.comparing;

@Data
@NoArgsConstructor
public class DbOperation {
    private static final Comparator<DbOperation> comparator = comparing(DbOperation::getTableName)
            .thenComparing(DbOperation::getEventType);

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

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof DbOperation)) {
            return false;
        }
        DbOperation ob = (DbOperation) o;
        return comparator.compare(this, ob) == 0 && this.data.equals(ob.getData());
    }

}
