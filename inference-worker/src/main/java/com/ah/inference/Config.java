package com.ah.inference;

import java.time.Duration;

public class Config {
    public static final String BOOTSTRAP_SERVERS = "localhost:29092,localhost:39092,localhost:49092";
    public static final String COMSUMER_GROUP = "inference-request-consumer";
    public static final String INFERENCE_REQUEST_TOPIC = "inference-request";
    public static final Duration POLL_DURATION = Duration.ofMillis(1000);

    public static final String MODEL_PATH = "/resnet101-v1-7.onnx";

    public static final String INFERENCE_RESULT_TOPIC = "inference-result";
    public static final String PRODUCER_CLIENT_ID = "inference-result-producer";

    public static String describe() {
        return String.format("\n" +
                "%-25s %s\n" +
                "%-25s %s\n" +
                "%-25s %s\n" +
                "%-25s %s ms\n" +
                "%-25s %s\n" +
                "%-25s %s\n" +
                "%-25s %s\n",
                "BOOTSTRAP_SERVERS:", BOOTSTRAP_SERVERS,
                "COMSUMER_GROUP:", COMSUMER_GROUP,
                "INFERENCE_REQUEST_TOPIC:", INFERENCE_REQUEST_TOPIC,
                "POLL_DURATION:", POLL_DURATION.toMillis(),
                "MODEL_PATH:", MODEL_PATH,
                "INFERENCE_RESULT_TOPIC:", INFERENCE_RESULT_TOPIC,
                "PRODUCER_CLIENT_ID:", PRODUCER_CLIENT_ID);
    }
}
