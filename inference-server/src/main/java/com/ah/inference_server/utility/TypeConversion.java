package com.ah.inference_server.utility;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

import javax.imageio.ImageIO;

public class TypeConversion {
    public static Optional<UUID> convertUUIDString(String uuid) {
        try {
            return Optional.of(UUID.fromString(uuid));
        } catch (IllegalArgumentException e) {
            return Optional.empty();
        }
    }

    public static Optional<BufferedImage> convertImageBytes(byte[] bytes) {
        try {
            ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
            return Optional.of(ImageIO.read(bais));
        } catch (IOException e) {
            return Optional.empty();
        }
    }
}
