package com.ah.inference;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.InputStream;
import java.io.ObjectInputStream;

import org.junit.Before;
import org.junit.Test;

import com.ah.inference.prediction.Predictor;
import com.ah.message.InferenceRequest;

public class AppTest {

    private InferenceRequest testInputInferenceRequest;
    private String testPredictionResult;

    @Before
    public void setUp() throws Exception {
        try (InputStream in = getClass().getClassLoader().getResourceAsStream("fox.ser")) {
            ObjectInputStream ois = new ObjectInputStream(in);
            testInputInferenceRequest = (InferenceRequest) ois.readObject();
        }
        testPredictionResult = "red fox";
    }

    @Test
    public void testCorrectPrediction_andCreateInferenceResult() throws Exception {
        var predictor = new Predictor(Config.MODEL_PATH);
        var predictResult = predictor.predict(testInputInferenceRequest.inputTensor());
        predictor.close();
        assertTrue(predictResult.isPresent());

        var inferenceResult = App.createInferenceResult(testInputInferenceRequest, predictResult.get());

        assertEquals(inferenceResult.id(), testInputInferenceRequest.id());
        assertEquals(inferenceResult.seq(), testInputInferenceRequest.seq());
        assertEquals(inferenceResult.predictions().length, testInputInferenceRequest.topk());
        assertEquals(inferenceResult.predictions()[0], testPredictionResult);
    }
}
