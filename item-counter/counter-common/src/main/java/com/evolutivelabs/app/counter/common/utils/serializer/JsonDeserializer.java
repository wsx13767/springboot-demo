package com.evolutivelabs.app.counter.common.utils.serializer;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.common.serialization.Deserializer;

import java.util.Map;

public class JsonDeserializer<T> implements Deserializer<T> {
    private ObjectMapper objectMapper;
    private Class<T> deserializedClass;
    public JsonDeserializer(Class<T> deserializedClass) {
        objectMapper = new ObjectMapper();
        this.deserializedClass = deserializedClass;
    }

    public JsonDeserializer() {

    }

    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {
        Deserializer.super.configure(configs, isKey);
    }

    @Override
    public T deserialize(String s, byte[] bytes) {
        if (bytes == null) {
            return null;
        }
        try {
            return objectMapper.readValue(bytes, this.deserializedClass);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void close() {
        Deserializer.super.close();
    }
}
