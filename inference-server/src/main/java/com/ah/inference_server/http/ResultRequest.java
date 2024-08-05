package com.ah.inference_server.http;

import java.util.UUID;

import com.ah.inference_server.utility.TypeConversion;

public record ResultRequest(String uuid, int seqStart, int seqEnd) {
    public UUID validatedUuid() throws IllegalArgumentException {
        return TypeConversion.convertUUIDString(uuid)
                .orElseThrow(() -> new IllegalArgumentException("Invalid UUID"));
    }
}
