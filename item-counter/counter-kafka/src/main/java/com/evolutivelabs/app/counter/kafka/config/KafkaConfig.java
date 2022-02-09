package com.evolutivelabs.app.counter.kafka.config;

import com.evolutivelabs.app.counter.common.utils.StreamsSerdesUtils;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafkaStreams;
import org.springframework.kafka.annotation.KafkaStreamsDefaultConfiguration;
import org.springframework.kafka.config.KafkaStreamsConfiguration;

import java.util.HashMap;
import java.util.Map;

@EnableKafkaStreams
@Configuration
public class KafkaConfig {
    @Autowired
    private KafkaStreamProperties kafkaStreamProperties;


    @Bean(name = KafkaStreamsDefaultConfiguration.DEFAULT_STREAMS_CONFIG_BEAN_NAME)
    KafkaStreamsConfiguration kStreamsConfig() {
        Map<String, Object> props = new HashMap<>();
        props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaStreamProperties.getBootstrap_server());
        props.put(StreamsConfig.APPLICATION_ID_CONFIG, kafkaStreamProperties.getApplication_id());
        props.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName());
        // 與consumer 無關，stream processor處理完後轉換有關
        props.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, StreamsSerdesUtils.BoxInfoCountSerde().getClass().getName());
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, kafkaStreamProperties.getAuto_offset_rest());
        // 設定key value Store 儲存位置
        props.put(StreamsConfig.STATE_DIR_CONFIG, kafkaStreamProperties.getState_dir());
        return new KafkaStreamsConfiguration(props);
    }
}
