package com.reply.io.jsondeserializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.reply.io.dto.EventTypeEnum;
import com.reply.io.dto.IoEventDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;

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
            throws IOException, JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        IoEventDto ioEventDto = new IoEventDto();
        String topic = String.valueOf(ctxt.findInjectableValue("topic", null, null));

        ioEventDto.setTableName(topic);
        JsonNode rootNode = jp.getCodec().readTree(jp);
        JsonNode rowsNode = rootNode.path(topic);
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
