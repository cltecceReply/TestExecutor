package com.reply.services.impl;

import com.reply.io.model.DbOperation;
import com.reply.services.writes.kafka.IOProviderService;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import com.reply.Application;
import com.reply.annotations.IgnoreDuringTest;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.junit.Assert;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.*;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.*;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.utility.DockerImageName;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Slf4j
@RunWith(SpringJUnit4ClassRunner.class)
@Import(IOProviderServiceIT.KafkaTestConfigs.class)
@SpringBootTest(classes = Application.class)
@ComponentScan(excludeFilters = @ComponentScan.Filter(IgnoreDuringTest.class))
@TestPropertySource(properties = {
        "kafka.enabled=true",
        "kafka.topic=T_GES_DOCU"
})
@DirtiesContext
public class IOProviderServiceIT {

    protected static final String TOPIC = "T_GES_DOCU";
    protected static final String KAFKA_CONTAINER_NAME = "confluentinc/cp-kafka:5.4.3";

    @ClassRule
    public static KafkaContainer kafka = new KafkaContainer(DockerImageName.parse(KAFKA_CONTAINER_NAME));

    @TestConfiguration
    static class KafkaTestConfigs {
        @Bean
        @Lazy
        ConcurrentKafkaListenerContainerFactory<Integer, String> eventListener() {
            ConcurrentKafkaListenerContainerFactory<Integer, String> factory = new ConcurrentKafkaListenerContainerFactory<>();
            factory.setConsumerFactory(consumerFactory());
            return factory;
        }

        @Bean
        @Lazy
        public ConsumerFactory<Integer, String> consumerFactory() {
            return new DefaultKafkaConsumerFactory<>(consumerConfigs());
        }

        @Bean
        @Lazy
        public Map<String, Object> consumerConfigs() {
            Map<String, Object> props = new HashMap<>();
            props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafka.getBootstrapServers());
            props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
            props.put(ConsumerConfig.GROUP_ID_CONFIG, "WriteTracerTest");
            props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
            props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
            return props;
        }

        @Bean
        @Lazy
        public ProducerFactory<String, String> producerFactory() {
            Map<String, Object> configProps = new HashMap<>();
            configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafka.getBootstrapServers());
            configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
            configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
            return new DefaultKafkaProducerFactory<>(configProps);
        }

        @Bean
        @Lazy
        public KafkaTemplate<String, String> kafkaTemplate(ProducerFactory<String, String> producerFactory) {
            return new KafkaTemplate<>(producerFactory);
        }
    }

    @Autowired
    KafkaTemplate<String, String> producer;
    @Autowired
    IOProviderService providerService;


    @Test
    public void consumerTest() {
        String payload = "{\"T_GES_DOCU\": {\"eventType\": \"INSERT\",\"data\": { \"DOCU_NUM_DOC_SE\": \"ZX\",\"DOCU_NUM_DOC\": \"  \",\"DOCU_DES_DOC\": \"                                                  \",\"DOCU_QTA_DOC\": \"0\",\"DOCU_TIT_DOC\": \"0\",\"DOCU_FLG_STAMPA\": \"0\",\"DOCU_COD_DOCUM\": \"   \",\"DOCU_FLG_OPZ\": \"0\" } } }{\"T_GES_DOCU\": {\"eventType\": \"DELETE\",\"data\": { \"DOCU_NUM_DOC_SE\": \"ZX\",\"DOCU_NUM_DOC\": \"  \",\"DOCU_DES_DOC\": \"                                                  \",\"DOCU_QTA_DOC\": \"0\",\"DOCU_TIT_DOC\": \"0\",\"DOCU_FLG_STAMPA\": \"0\",\"DOCU_COD_DOCUM\": \"   \",\"DOCU_FLG_OPZ\": \"0\" } } }";
        log.info("sending payload='{}' to topic='{}'", payload, TOPIC);
        producer.send(TOPIC, payload);
        try {
            TimeUnit.SECONDS.sleep(3);
        } catch (InterruptedException ie) {
            log.error("Unable to wait");
            Thread.currentThread().interrupt();
        }
        List<DbOperation> list = providerService.getAndFlush();
        Assert.assertFalse(list.isEmpty());
    }

}
