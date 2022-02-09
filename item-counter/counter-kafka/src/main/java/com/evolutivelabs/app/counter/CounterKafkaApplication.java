package com.evolutivelabs.app.counter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class CounterKafkaApplication {
    public static void main(String[] args) {
        SpringApplication.run(CounterKafkaApplication.class, args);
    }
}
