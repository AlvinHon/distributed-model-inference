package com.ah.inference;

import java.util.List;
import java.util.logging.Logger;

import com.ah.inference.kafka.ConsumerRunner;
import com.ah.inference.kafka.KafkaFactory;
import com.ah.inference.kafka.ProducerRecordSender;
import com.ah.inference.prediction.Prediction;
import com.ah.inference.prediction.Predictor;
import com.ah.message.InferenceRequest;
import com.ah.message.InferenceResult;

public class App {
    private static final Logger logger = Logger.getLogger(App.class.getName());

    public static void main(String[] args) throws Exception {
        logger.info(Config.describe());

        var consumer = KafkaFactory.createInferenceRequestConsumer(Config.BOOTSTRAP_SERVERS, Config.COMSUMER_GROUP);
        var producer = KafkaFactory.createInferenceResultProducer(Config.BOOTSTRAP_SERVERS, Config.PRODUCER_CLIENT_ID);

        var runner = new ConsumerRunner<>(consumer, List.of(Config.INFERENCE_REQUEST_TOPIC));
        var predictor = new Predictor(Config.MODEL_PATH);
        var sender = new ProducerRecordSender<>(producer, Config.INFERENCE_RESULT_TOPIC);

        logger.info("Start Inference Service.");
        runner.run(Config.POLL_DURATION, (key, inferenceRequest) -> {
            logger.info("Received Inference Request: "
                    + inferenceRequest.id() + " "
                    + inferenceRequest.seq() + " "
                    + inferenceRequest.topk());
            try {
                predictor.predict(inferenceRequest.inputTensor())
                        .map((predictions) -> createInferenceResult(inferenceRequest, predictions))
                        .ifPresent((inferenceResult) -> sender.send(key, inferenceResult));
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        // unreachable
        predictor.close();
    }

    public static InferenceResult createInferenceResult(
            InferenceRequest inferenceRequest,
            List<Prediction> predictions) {
        var results = predictions.subList(0, inferenceRequest.topk())
                .stream()
                .map((prediction) -> prediction.name())
                .toArray(String[]::new);
        return new InferenceResult(inferenceRequest.id(), inferenceRequest.seq(), results);
    }
}
