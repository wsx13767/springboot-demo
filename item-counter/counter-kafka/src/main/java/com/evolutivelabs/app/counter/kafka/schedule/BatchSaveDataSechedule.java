package com.evolutivelabs.app.counter.kafka.schedule;

import com.evolutivelabs.app.counter.database.mysql.entity.ItemKafkaLog;
import com.evolutivelabs.app.counter.database.mysql.entity.ItemLog;
import com.evolutivelabs.app.counter.database.mysql.repository.ItemKafkaLogRepository;
import com.evolutivelabs.app.counter.database.mysql.repository.ItemLogRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * 每10秒將itemlog寫入db
 */
@Component
public class BatchSaveDataSechedule {
    private static final Logger logger = LoggerFactory.getLogger(BatchSaveDataSechedule.class);
    @Autowired
    private ItemKafkaLogRepository itemKafkaLogRepository;

    private ReadWriteLock lock = new ReentrantReadWriteLock();
    private List<ItemKafkaLog> syncList = new ArrayList<>();
    private Lock writeLock = lock.writeLock();
    private Lock readLock = lock.readLock();

    public void add(ItemKafkaLog items) {
        try {
            writeLock.lock();
            syncList.add(items);
        } finally {
            writeLock.unlock();
        }
    }

    @Scheduled(fixedRate = 10000)
    void process() {
        List<ItemKafkaLog> newList = new ArrayList<>();
        try {
            writeLock.lock();
            newList = new ArrayList<>(syncList);
            syncList.clear();
        } finally {
            writeLock.unlock();
        }

        if (newList.size() > 0) {
            itemKafkaLogRepository.saveAll(newList);
        }
    }
}
