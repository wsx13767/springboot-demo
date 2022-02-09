package com.evolutivelabs.app.counter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;

import java.lang.reflect.Constructor;

@SpringBootApplication
public class CounterBatchApplication {

    public static void main(String[] args) {
        SpringApplication.run(CounterBatchApplication.class, args);
    }

}
