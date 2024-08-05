package com.ah.inference.prediction;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.IntStream;

import ai.onnxruntime.OnnxTensor;
import ai.onnxruntime.OrtEnvironment;
import ai.onnxruntime.OrtException;
import ai.onnxruntime.OrtSession;

public class Predictor implements AutoCloseable {
    private OrtEnvironment env;
    private OrtSession session;
    private String inputName;
    private String outputName;

    public Predictor(String modelPath) throws OrtException, IOException {
        byte[] modelBytes = null;
        try (InputStream in = getClass().getResourceAsStream(modelPath)) {
            modelBytes = in.readAllBytes();
        }

        env = OrtEnvironment.getEnvironment();
        session = env.createSession(modelBytes);
        inputName = session.getInputNames().iterator().next(); // 1 input
        outputName = session.getOutputNames().iterator().next(); // 1 input
    }

    public Optional<List<Prediction>> predict(float[][][][] input) throws OrtException {
        OnnxTensor inputTensor = OnnxTensor.createTensor(env, input);

        var results = session.run(Map.of(inputName, inputTensor));

        var outputTensor = results.get(outputName);
        if (outputTensor.isEmpty()) {
            return Optional.empty();
        }

        var values = (float[][]) outputTensor.get().getValue();
        if (values.length == 0) {
            return Optional.empty();
        }

        return Optional.of(IntStream
                .range(0, values[0].length)
                .mapToObj((i) -> {
                    var score = values[0][i];
                    var name = PredictionLabels.getFirstName(i);
                    return new Prediction(name, score);
                })
                .sorted((a, b) -> Double.compare(b.score(), a.score()))
                .toList());
    }

    @Override
    public void close() throws Exception {
        if (session != null) {
            session.close();
        }
        if (env != null) {
            env.close();
        }
    }
}
