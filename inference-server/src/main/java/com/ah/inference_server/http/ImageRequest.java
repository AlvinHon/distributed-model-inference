package com.ah.inference_server.http;

import java.awt.image.BufferedImage;
import java.util.UUID;

import com.ah.inference_server.utility.TypeConversion;

public record ImageRequest(String uuid, int seq, int topK, byte[] payload) {
    public UUID validatedUuid() throws IllegalArgumentException {
        return TypeConversion.convertUUIDString(uuid)
                .orElseThrow(() -> new IllegalArgumentException("Invalid UUID"));
    }

    public BufferedImage validatedImage() throws IllegalArgumentException {
        return TypeConversion.convertImageBytes(payload)
                .orElseThrow(() -> new IllegalArgumentException("Invalid image"));
    }

    public int validatedTopK() throws IllegalArgumentException {
        if (topK < 1 || topK > 1000) {
            throw new IllegalArgumentException("Invalid topK. Must be between 1 and 1000.");
        }
        return topK;
    }

    public int validatedSeq() throws IllegalArgumentException {
        if (seq < 0) {
            throw new IllegalArgumentException("Invalid seq. Must be greater than or equal to 0.");
        }
        return seq;
    }
}
