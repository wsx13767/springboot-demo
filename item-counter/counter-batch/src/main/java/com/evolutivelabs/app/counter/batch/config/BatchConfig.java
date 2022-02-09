package com.evolutivelabs.app.counter.batch.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.concurrent.ConcurrentTaskScheduler;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

@Configuration
@EnableScheduling
public class BatchConfig {

    /**
     * 設定排程同時間內可啟動的thread
     * @return
     */
    @Bean
    public TaskScheduler taskScheduler(ScheduledExecutorService executorService) {
        return new ConcurrentTaskScheduler(executorService);
    }

    /**
     * 設定排程thread pool 並設定destroy，交付給spring管理
     * @return
     */
    @Bean(destroyMethod = "shutdown")
    public ScheduledExecutorService scheduledExecutorService() {
        return Executors.newScheduledThreadPool(10);
    }
}
