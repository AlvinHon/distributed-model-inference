package com.ah.message;

import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;

import org.apache.kafka.common.serialization.Deserializer;

public interface ObjectDeserializer<T> extends Deserializer<T> {

    @SuppressWarnings("unchecked")
    @Override
    default T deserialize(String topic, byte[] data) {
        try (ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(data))) {
            return (T) ois.readObject();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
