package com.evolutivelabs.app.counter.common.utils;

import com.evolutivelabs.app.counter.common.model.ordercounter.BoxDetail;
import com.evolutivelabs.app.counter.common.model.ordercounter.BoxInfoCount;
import com.evolutivelabs.app.counter.common.model.ordercounter.InBoxItems;
import com.evolutivelabs.app.counter.common.utils.serializer.JsonDeserializer;
import com.evolutivelabs.app.counter.common.utils.serializer.JsonSerializer;
import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serializer;

import java.util.Map;

public class StreamsSerdesUtils {
    public static Serde<InBoxItems> InboxItemsSerde() {
        return new InboxItemsSerde();
    }

    public static Serde<BoxDetail> BoxDetailSerde() {
        return new BoxDetailSerde();
    }

    public static Serde<BoxInfoCount> BoxInfoCountSerde() {
        return new BoxInfoCountSerde();
    }

    public static final class BoxInfoCountSerde extends WrapperSerde<BoxInfoCount> {
        public BoxInfoCountSerde() {
            super(new JsonSerializer<>(), new JsonDeserializer<>(BoxInfoCount.class));
        }
    }

    public static final class BoxDetailSerde extends WrapperSerde<BoxDetail> {
        public BoxDetailSerde() {
            super(new JsonSerializer<>(), new JsonDeserializer<>(BoxDetail.class));
        }
    }

    public static final class InboxItemsSerde extends WrapperSerde<InBoxItems> {
        public InboxItemsSerde() {
            super(new JsonSerializer<>(), new JsonDeserializer<>(InBoxItems.class));
        }
    }

    private static class WrapperSerde<T> implements Serde<T> {

        private JsonSerializer<T> serializer;
        private JsonDeserializer<T> deserializer;

        WrapperSerde(JsonSerializer<T> serializer, JsonDeserializer<T> deserializer) {
            this.serializer = serializer;
            this.deserializer = deserializer;
        }

        @Override
        public void configure(Map<String, ?> map, boolean b) {

        }

        @Override
        public void close() {

        }

        @Override
        public Serializer<T> serializer() {
            return serializer;
        }

        @Override
        public Deserializer<T> deserializer() {
            return deserializer;
        }
    }
}
