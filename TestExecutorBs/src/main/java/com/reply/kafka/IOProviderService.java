package com.reply.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.InjectableValues;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.reply.io.model.DbOperation;
import com.reply.kafka.dto.IoEventDto;
import com.reply.kafka.jsondeserializer.IoEventJsonDeserializer;
import com.reply.kafka.jsondeserializer.IoEventJsonDeserializerV2;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@Service
@Slf4j
@ConditionalOnProperty(value="kafka.enabled", havingValue="true")
public class IOProviderService implements IIOProviderService{

    Queue<List<DbOperation>> ops = new LinkedList<>();
    AtomicLong idFactory = new AtomicLong();
    StdDeserializer<IoEventDto> deserializer;

    public IOProviderService(@Value("${kafka.format}") int kafkaFormat){
        switch(kafkaFormat){
            case 1:
                deserializer = new IoEventJsonDeserializerV2();
                break;
            default:
                deserializer = new IoEventJsonDeserializer();
                break;
        }

    }
    @KafkaListener(topics = "${kafka.topic}"
            , containerFactory = "eventListener")
    private void topicListener(String events) {
        List<DbOperation> newEvents = new ArrayList<>();
        try {
            newEvents = this.deserializeSymDsMessages(events);
            log.info("Received {} new events", newEvents.size());
        } catch (JsonProcessingException e) {
            log.error("Error processing message [{}]", events, e);
            throw new RuntimeException("Error processing new event");
        }
        this.ops.add(newEvents);
    }

    public List<DbOperation> getAndFlush(){
        List<DbOperation> els  = this.ops.poll();
        return els != null ? els : new ArrayList<>();
    }

    protected List<DbOperation> deserializeSymDsMessages(String msg) throws JsonProcessingException {
        String fixedMessage = "["+msg.replace("}{", "}, {")+"]";
        ObjectMapper mapper = new ObjectMapper();
        SimpleModule module = new SimpleModule();
        module.addDeserializer(IoEventDto.class, deserializer);
        IoEventDto[] event = mapper
                .registerModule(module)
                .readValue(fixedMessage, IoEventDto[].class);
        return Arrays.stream(event)
                .map(DbOperation::new)
                .map(el -> el.setIdAndReturn(this.idFactory.getAndIncrement()))
                .collect(Collectors.toList());
    }
}
