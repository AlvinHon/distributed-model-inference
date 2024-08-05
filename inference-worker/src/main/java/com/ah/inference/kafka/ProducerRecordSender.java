package com.ah.inference.kafka;

import java.util.logging.Logger;

import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;

public class ProducerRecordSender<K, V> {
    Logger logger = Logger.getLogger(ProducerRecordSender.class.getName());

    Producer<K, V> producer;
    String topic;

    public ProducerRecordSender(Producer<K, V> producer, String topic) {
        this.producer = producer;
        this.topic = topic;
    }

    public void send(K key, V value) {
        try {
            var metadata = producer.send(new ProducerRecord<>(topic, key, value)).get();
            logger.info(String.format("Record with (key: %s), was sent to " +
                    "(partition: %d, offset: %d, topic: %s)",
                    key,
                    metadata.partition(),
                    metadata.offset(),
                    metadata.topic()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
