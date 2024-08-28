package com.ah.message;

public record PreprocessRequest(String id, int seq, int topk, String searchKey) implements java.io.Serializable {
}
