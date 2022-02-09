package com.evolutivelabs.app.counter.kafka.config;

import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Component
@ConfigurationProperties(prefix = "evolutivelabs.kafka.streams")
public class KafkaStreamProperties {
    private String bootstrap_server;
    private String application_id;
    private String auto_offset_rest;
    private String storeName;
    private String consumer_topic;
    private String state_dir;
}
