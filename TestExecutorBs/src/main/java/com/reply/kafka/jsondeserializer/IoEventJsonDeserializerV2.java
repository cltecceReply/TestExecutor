package com.reply.kafka.jsondeserializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.reply.io.dto.EventTypeEnum;
import com.reply.kafka.dto.IoEventDto;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

@Slf4j
public class IoEventJsonDeserializerV2 extends StdDeserializer<IoEventDto> {

    public IoEventJsonDeserializerV2() {
        this(null);
    }

    public IoEventJsonDeserializerV2(Class<?> vc) {
        super(vc);
    }
    @Override
    public IoEventDto deserialize(JsonParser jp, DeserializationContext ctxt)
            throws IOException, JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        IoEventDto ioEventDto = new IoEventDto();

        JsonNode rootNode = jp.getCodec().readTree(jp);
        String tableName = rootNode.get("table_name").asText();
        ioEventDto.setTableName(tableName);
        ioEventDto.getOperation().setEventType(EventTypeEnum.valueOf(rootNode.get("eventType").asText()));
        JsonNode rowsNode = rootNode.path("data");
        Map<String, String> values =  mapper.treeToValue(rowsNode, HashMap.class);
        ioEventDto.getOperation().setData(values);
        return ioEventDto;
    }
}
