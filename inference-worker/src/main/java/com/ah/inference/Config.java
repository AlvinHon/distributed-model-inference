package com.ah.inference;

import java.time.Duration;
import java.util.Optional;

public class Config {
    public static final String BOOTSTRAP_SERVERS;
    public static final String COMSUMER_GROUP = "inference-request-consumer";
    public static final String INFERENCE_REQUEST_TOPIC = "inference-request";
    public static final Duration POLL_DURATION;

    public static final String MODEL_PATH = "/resnet101-v1-7.onnx";

    public static final String INFERENCE_RESULT_TOPIC = "inference-result";
    public static final String PRODUCER_CLIENT_ID = "inference-result-producer";

    static {
        BOOTSTRAP_SERVERS = Optional.ofNullable(System.getenv("BOOTSTRAP_SERVERS"))
                .orElse("localhost:29092,localhost:39092,localhost:49092");
        POLL_DURATION = Duration.ofMillis(
                Optional.ofNullable(System.getenv("POLL_DURATION"))
                        .map(Long::parseLong)
                        .orElse(1000L));
    }

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
