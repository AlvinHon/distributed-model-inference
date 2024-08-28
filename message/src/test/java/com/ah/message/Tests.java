package com.ah.message;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

/**
 * Unit tests
 */
public class Tests {

    @Test
    public void testInferenceRequest() {
        InferenceRequest inferenceRequest = new InferenceRequest("Hello, World!", 1, 2,
                new float[][][][] { { { { 1.0f } } } });
        byte[] bytes = null;
        try (InferenceRequestSerializer serializer = new InferenceRequestSerializer()) {
            bytes = serializer.serialize(null, inferenceRequest);
        }
        InferenceRequest deserialized = null;
        try (InferenceRequestDeserializer deserializer = new InferenceRequestDeserializer()) {
            deserialized = deserializer.deserialize(null, bytes);
        }
        assertEquals(inferenceRequest.id(), deserialized.id());
        assertEquals(inferenceRequest.seq(), deserialized.seq());
        assertEquals(inferenceRequest.topk(), deserialized.topk());
        assertArrayEquals(inferenceRequest.inputTensor(), inferenceRequest.inputTensor());
    }

    @Test
    public void testInferenceResult() {
        InferenceResult inferenceResult = new InferenceResult("Hello, World!", 1, new String[] { "apple", "banana" });
        byte[] bytes = null;
        try (InferenceResultSerializer serializer = new InferenceResultSerializer()) {
            bytes = serializer.serialize(null, inferenceResult);
        }
        InferenceResult deserialized = null;
        try (InferenceResultDeserializer deserializer = new InferenceResultDeserializer()) {
            deserialized = deserializer.deserialize(null, bytes);
        }
        assertEquals(inferenceResult.id(), deserialized.id());
        assertEquals(inferenceResult.seq(), deserialized.seq());
        assertArrayEquals(inferenceResult.predictions(), deserialized.predictions());
    }

    @Test
    public void testPreprocessRequest() {
        PreprocessRequest preprocessRequest = new PreprocessRequest("Hello, World!", 1, 2, "input");
        byte[] bytes = null;
        try (PreprocessRequestSerializer serializer = new PreprocessRequestSerializer()) {
            bytes = serializer.serialize(null, preprocessRequest);
        }
        PreprocessRequest deserialized = null;
        try (PreprocessRequestDeserializer deserializer = new PreprocessRequestDeserializer()) {
            deserialized = deserializer.deserialize(null, bytes);
        }
        assertEquals(preprocessRequest.id(), deserialized.id());
        assertEquals(preprocessRequest.seq(), deserialized.seq());
        assertEquals(preprocessRequest.topk(), deserialized.topk());
        assertEquals(preprocessRequest.searchKey(), preprocessRequest.searchKey());
    }
}
