package com.reply.services.writes.kafka.jsondeserializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.reply.io.dto.EventTypeEnum;
import com.reply.dto.IoEventDto;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

@Slf4j
public class IoEventJsonDeserializer extends StdDeserializer<IoEventDto> {

    public IoEventJsonDeserializer() {
        this(null);
    }

    public IoEventJsonDeserializer(Class<?> vc) {
        super(vc);
    }
    @Override
    public IoEventDto deserialize(JsonParser jp, DeserializationContext ctxt)
            throws IOException{
        ObjectMapper mapper = new ObjectMapper();
        IoEventDto ioEventDto = new IoEventDto();

        JsonNode rootNode = jp.getCodec().readTree(jp);
        String tableName = rootNode.fieldNames().next();
        ioEventDto.setTableName(tableName);
        JsonNode rowsNode = rootNode.path(tableName);
        Iterator<JsonNode> elements = rowsNode.elements();
        if(!elements.hasNext())
            throw new IOException("Unexpected end of the record");
        ioEventDto.getOperation().setEventType(EventTypeEnum.valueOf(elements.next().asText()));
        if(!elements.hasNext())
            throw new IOException("Unexpected end of the record");
        Map<String, String> values =  mapper.treeToValue(elements.next(), HashMap.class);
        ioEventDto.getOperation().setData(values);
        return ioEventDto;
    }
}
