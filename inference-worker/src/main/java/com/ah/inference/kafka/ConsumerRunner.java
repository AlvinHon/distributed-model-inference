package com.ah.inference.kafka;

import java.time.Duration;
import java.util.List;
import java.util.function.BiConsumer;

import org.apache.kafka.clients.consumer.Consumer;

public class ConsumerRunner<K, V> {

    private Consumer<K, V> consumer;
    private List<String> topics;

    public ConsumerRunner(Consumer<K, V> consumer, List<String> topics) {
        this.consumer = consumer;
        this.topics = topics;
    }

    public void run(Duration poll_duration, BiConsumer<K, V> consumerFunction) {
        consumer.subscribe(topics);

        while (true) {
            consumer.poll(poll_duration).forEach((record) -> {
                if (record.value() == null) {
                    return;
                }
                consumerFunction.accept(record.key(), record.value());
            });

            consumer.commitAsync();
        }
    }
}