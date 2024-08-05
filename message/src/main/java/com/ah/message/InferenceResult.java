package com.ah.message;

public record InferenceResult(String id, int seq, String[] predictions) implements java.io.Serializable {
}
