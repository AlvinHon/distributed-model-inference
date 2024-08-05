package com.ah.inference_server.service;

import java.awt.image.BufferedImage;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.apache.kafka.clients.producer.RecordMetadata;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.ah.inference_server.utility.ImageProcessor;
import com.ah.message.InferenceRequest;

@Service
public class InferenceRequestService {
    final static Logger logger = org.slf4j.LoggerFactory.getLogger(InferenceRequestService.class);

    @Value(value = "${spring.kafka.topic-inference-request}")
    private String topicName;

    @Value(value = "${spring.kafka.producer-send-timeout-ms}")
    private long sendTimeoutMS;

    @Autowired
    private KafkaTemplate<String, InferenceRequest> kafkaTemplate;

    public void sendRequest(BufferedImage image, UUID uuid, int seq, int topK) throws Exception {
        InferenceRequest request = createInferenceRequest(uuid.toString(), seq, topK, image);

        var sendResult = kafkaTemplate
                .send(topicName, request)
                .get(sendTimeoutMS, TimeUnit.MILLISECONDS);

        RecordMetadata metadata = sendResult.getRecordMetadata();
        logger.info(String.format(
                "Record with (key: %s), was sent to " + "(partition: %d, offset: %d, topic: %s)",
                sendResult.getProducerRecord().key(),
                metadata.partition(), metadata.offset(), metadata.topic()));
    }

    InferenceRequest createInferenceRequest(String id, int seq, int topK, BufferedImage image) {
        var inputTensor = new ImageProcessor(image)
                .crop()
                .resize(224, 224)
                .getNormalizedRGBFloats();
        return new InferenceRequest(id, seq, topK, inputTensor);
    }
}
