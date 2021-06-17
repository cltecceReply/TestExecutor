package com.reply.configs;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@EnableKafka
@Configuration
public class KafkaConfig {


    @Value("${kafka.broker:localhost:9092}")
    protected String kafkaBootstrapServer;

    @Bean
    @ConditionalOnProperty(value="kafka.enabled", havingValue="true")
    public ConsumerFactory<String, String> eventConsumer() {
        log.info("Kafka Bootstrap Server: {}", kafkaBootstrapServer);
        // HashMap to store the configurations
        Map<String, Object> map
                = new HashMap<>();

        // put the host IP in the map
        map.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, getBootstrapServer());
        // put the group ID of consumer in the map
        map.put(ConsumerConfig.GROUP_ID_CONFIG, "WriteTracer");
        map.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        map.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        // return message in JSON formate
        return new DefaultKafkaConsumerFactory<>(
                map, new StringDeserializer(),
                new StringDeserializer());
    }

    @Bean
    @ConditionalOnMissingBean
    public ConcurrentKafkaListenerContainerFactory<String, String> eventListener(ConsumerFactory<String, String> consumer) {
        ConcurrentKafkaListenerContainerFactory<String,
                String> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumer);
        return factory;
    }

    protected String getBootstrapServer(){
        return kafkaBootstrapServer;
    }
}
