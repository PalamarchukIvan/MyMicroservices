package org.example;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.common.serialization.LongSerializer;

import java.util.Properties;

import static org.apache.kafka.clients.CommonClientConfigs.BOOTSTRAP_SERVERS_CONFIG;
import static org.apache.kafka.clients.CommonClientConfigs.CLIENT_ID_CONFIG;
import static org.apache.kafka.clients.CommonClientConfigs.RETRIES_CONFIG;
import static org.apache.kafka.clients.producer.ProducerConfig.*;
import static org.example.JsonSerializer.OBJECT_MAPPER;

public class Producer {
    private final KafkaProducer<Long, DataSender.StringValue> kafkaProducer;
    public static final String TOPIC_NAME = "topic";

    public Producer(String bootstrapServers) {
        Properties props = new Properties();
        props.put(CLIENT_ID_CONFIG, "myKafkaProducer");
        props.put(BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ACKS_CONFIG, "1");
        props.put(RETRIES_CONFIG, 1);
        props.put(BATCH_SIZE_CONFIG, 16384);
        props.put(LINGER_MS_CONFIG, 10);
        props.put(BUFFER_MEMORY_CONFIG, 33_554_432); // bytes
        props.put(MAX_BLOCK_MS_CONFIG, 1_000); // ms
        props.put(KEY_SERIALIZER_CLASS_CONFIG, LongSerializer.class);
        props.put(VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        props.put(OBJECT_MAPPER, new ObjectMapper());

        this.kafkaProducer = new KafkaProducer<>(props);

        var shutdownHook =
                new Thread(
                        () -> {
                            System.err.println("Close");
                            kafkaProducer.close();
                        });
        Runtime.getRuntime().addShutdownHook(shutdownHook);
    }


    public KafkaProducer<Long, DataSender.StringValue> getMyProducer() {
        return kafkaProducer;
    }

}
