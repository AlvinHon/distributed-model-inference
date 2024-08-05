package com.ah.message;

import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;

import org.apache.kafka.common.serialization.Serializer;

public interface ObjectSerializer<T> extends Serializer<T> {

    @Override
    default byte[] serialize(String topic, T data) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try (ObjectOutputStream oos = new ObjectOutputStream(baos)) {
            oos.writeObject(data);
            return baos.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
