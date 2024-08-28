package com.ah.inference_server.service;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.apache.kafka.clients.producer.RecordMetadata;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.ah.message.PreprocessRequest;

@Service
public class PreprocessRequestService {
    final static Logger logger = org.slf4j.LoggerFactory.getLogger(PreprocessRequestService.class);

    @Value(value = "${spring.kafka.topic-preprocess-request}")
    private String topicName;

    @Value(value = "${spring.kafka.producer-send-timeout-ms}")
    private long sendTimeoutMS;

    @Autowired
    private KafkaTemplate<String, PreprocessRequest> kafkaTemplate;

    public void send(UUID uuid, int seq, int topK, String searchKey) throws Exception {
        PreprocessRequest request = new PreprocessRequest(uuid.toString(), seq, topK, searchKey);

        var sendResult = kafkaTemplate
                .send(topicName, request)
                .get(sendTimeoutMS, TimeUnit.MILLISECONDS);

        RecordMetadata metadata = sendResult.getRecordMetadata();
        logger.info(String.format(
                "Record with (key: %s), was sent to " + "(partition: %d, offset: %d, topic: %s)",
                sendResult.getProducerRecord().key(),
                metadata.partition(), metadata.offset(), metadata.topic()));
    }
}
