package com.ah.preprocessor;

import java.util.Optional;
import java.util.Properties;
import java.util.function.Function;
import java.util.logging.Logger;

import javax.imageio.ImageIO;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;

import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.Produced;
import org.apache.kafka.streams.kstream.ValueMapper;

import com.ah.message.InferenceRequest;
import com.ah.message.InferenceRequestDeserializer;
import com.ah.message.InferenceRequestSerializer;
import com.ah.message.PreprocessRequest;
import com.ah.message.PreprocessRequestDeserializer;
import com.ah.message.PreprocessRequestSerializer;

public class App {
    private static final Logger logger = Logger.getLogger(App.class.getName());

    public static void main(String[] args) throws Exception {
        var storage = new DocumentStorage();
        var builder = streamsBuilder(storage::find);
        var streams = new KafkaStreams(builder.build(), streamsConfig());

        onShutdown(() -> {
            streams.close();
            storage.close();
            System.out.println("Complete shutdown");
        });

        streams.start();
        logger.info("Started Kafka Streams");

        // Keep the main thread alive for processing requests in the background.
        while (true) {
            Thread.sleep(1000);
        }
    }

    static void onShutdown(Runnable action) {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            action.run();
        }));
    }

    /**
     * Setup a streams builder that reads preprocess requests from stream and call
     * `findData` to get the image data for preprocessing.
     */
    static StreamsBuilder streamsBuilder(Function<String, Optional<byte[]>> findData) {
        StreamsBuilder builder = new StreamsBuilder();
        builder.stream(Config.Kafka.PREPROCESS_REQUEST_TOPIC,
                Consumed.with(
                        Serdes.String(),
                        Serdes.serdeFrom(
                                new PreprocessRequestSerializer(),
                                new PreprocessRequestDeserializer())))
                .mapValues(new ValueMapper<PreprocessRequest, InferenceRequest>() {
                    @Override
                    public InferenceRequest apply(PreprocessRequest preprocessRequest) {
                        logger.info("Processing request: " + preprocessRequest.id());
                        return findData
                                .apply(preprocessRequest.searchKey())
                                .flatMap(App::convertImageBytes)
                                .map(image -> createInferenceRequest(image, preprocessRequest))
                                .orElse(null);
                    }
                }).to(Config.Kafka.INFERENCE_REQUEST_TOPIC,
                        Produced.with(
                                Serdes.String(),
                                Serdes.serdeFrom(
                                        new InferenceRequestSerializer(),
                                        new InferenceRequestDeserializer())));

        return builder;
    }

    static Properties streamsConfig() {
        Properties streamsConfig = new Properties();
        streamsConfig.put(StreamsConfig.APPLICATION_ID_CONFIG,
                Config.Kafka.APPLICATION_ID);
        streamsConfig.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG,
                Config.Kafka.BOOTSTRAP_SERVERS);
        return streamsConfig;
    }

    static Optional<BufferedImage> convertImageBytes(byte[] bytes) {
        try {
            ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
            return Optional.of(ImageIO.read(bais));
        } catch (IOException e) {
            return Optional.empty();
        }
    }

    static InferenceRequest createInferenceRequest(BufferedImage image, PreprocessRequest preprocessRequest) {
        var tensorData = new ImageProcessor(image)
                .crop()
                .resize(224, 224)
                .getNormalizedRGBFloats();
        return new InferenceRequest(
                preprocessRequest.id(),
                preprocessRequest.seq(),
                preprocessRequest.topk(),
                tensorData);
    }
}
