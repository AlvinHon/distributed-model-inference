package com.ah.inference.kafka;

import java.util.Map;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;

import com.ah.message.InferenceRequest;
import com.ah.message.InferenceRequestDeserializer;
import com.ah.message.InferenceResult;
import com.ah.message.InferenceResultSerializer;

public class KafkaFactory {

    public static Consumer<String, InferenceRequest> createInferenceRequestConsumer(
            String bootstrapServers,
            String consumerGroup) {

        return new KafkaConsumer<>(Map.of(
                ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers,
                ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName(),
                ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, InferenceRequestDeserializer.class.getName(),
                ConsumerConfig.GROUP_ID_CONFIG, consumerGroup,
                ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, true));
    }

    public static Producer<String, InferenceResult> createInferenceResultProducer(
            String bootstrapServers,
            String clientId) {

        return new KafkaProducer<>(Map.of(
                ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers,
                ProducerConfig.CLIENT_ID_CONFIG, clientId,
                ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName(),
                ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, InferenceResultSerializer.class.getName()));
    }
}
