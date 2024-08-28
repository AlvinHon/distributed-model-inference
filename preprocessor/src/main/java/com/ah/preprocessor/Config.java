package com.ah.preprocessor;

import java.util.Optional;

public class Config {

    public class Kafka {
        public static final String BOOTSTRAP_SERVERS;
        public static final String APPLICATION_ID = "preprocessor";
        public static final String COMSUMER_GROUP = "preprocess-request-consumer";
        public static final String PREPROCESS_REQUEST_TOPIC = "preprocess-request";

        public static final String INFERENCE_REQUEST_TOPIC = "inference-request";
        public static final String PRODUCER_CLIENT_ID = "inference-request-producer";

        static {
            BOOTSTRAP_SERVERS = Optional.ofNullable(System.getenv("BOOTSTRAP_SERVERS"))
                    .orElse("localhost:29092,localhost:39092,localhost:49092");
        }

        public static String describe() {
            return String.format("\n" +
                    "%-25s %s\n" +
                    "%-25s %s\n" +
                    "%-25s %s\n" +
                    "%-25s %s\n" +
                    "%-25s %s\n",
                    "BOOTSTRAP_SERVERS:", BOOTSTRAP_SERVERS,
                    "COMSUMER_GROUP:", COMSUMER_GROUP,
                    "PREPROCESS_REQUEST_TOPIC:", PREPROCESS_REQUEST_TOPIC,
                    "INFERENCE_REQUEST_TOPIC:", INFERENCE_REQUEST_TOPIC,
                    "PRODUCER_CLIENT_ID:", PRODUCER_CLIENT_ID);
        }
    }

    public class MongoDB {
        public static final String HOST = "localhost:27017";
        public static final String DATABASE = "inference-mongodb";
        public static final String COLLECTION = "preprocess_data";
        private static final String USERNAME = "user";
        private static final String PASSWORD = "password";

        public static String connectionString() {
            return String.format("mongodb://%s:%s@%s/%s", USERNAME, PASSWORD, HOST, DATABASE);
        }

    }

}
