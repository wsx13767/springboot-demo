package com.evolutivelabs.app.counter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class CounterApiApplication {
    public static void main(String[] args) {
        SpringApplication.run(CounterApiApplication.class, args);
    }
}
