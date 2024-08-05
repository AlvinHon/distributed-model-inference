package com.ah.message;

public record InferenceRequest(String id, int seq, int topk, float[][][][] inputTensor)
        implements java.io.Serializable {
}