package com.reply.services.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.InjectableValues;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.reply.io.dto.IoEventDto;
import com.reply.io.dto.IoOperationDto;
import com.reply.io.model.DbOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@Service
@Slf4j
@ConditionalOnProperty(value="kafka.enabled", havingValue="true")
public class IOProviderService {

    List<DbOperation> ops = new LinkedList<>();
    AtomicLong idFactory = new AtomicLong();
    @Value("${kafka.topic:writes}")
    String topic;

    @KafkaListener(topics = "${kafka.topic}"
            , containerFactory = "eventListener")
    private void createConsumer(String events) {
        List<DbOperation> newEvents = new ArrayList<>();
        try {
            newEvents = this.deserializeSymDsMessages(events);
            log.info("Received {} new events", newEvents.size());
        } catch (JsonProcessingException e) {
            log.error("Error processing message [{}]", events, e);
            throw new RuntimeException("Error processing new event");
        }
        this.ops.addAll(newEvents);
    }

    public List<DbOperation> getAndFlush(){
        List<DbOperation> currOps = this.ops;
        this.ops = new LinkedList<>();
        return currOps;
    }

    protected List<DbOperation> deserializeSymDsMessages(String msg) throws JsonProcessingException {
        String fixedMessage = "["+msg.replace("}{", "}, {")+"]";
        ObjectMapper mapper = new ObjectMapper();
        IoEventDto[] event = mapper
                .setInjectableValues(this.injectEventType())
                .readValue(fixedMessage, IoEventDto[].class);
        return Arrays.stream(event)
                .map(DbOperation::new)
                .map(el -> el.setIdAndReturn(this.idFactory.getAndIncrement()))
                .collect(Collectors.toList());
    }

    protected InjectableValues injectEventType() {
        return new InjectableValues.Std()
                .addValue("topic", topic);
    }
}
