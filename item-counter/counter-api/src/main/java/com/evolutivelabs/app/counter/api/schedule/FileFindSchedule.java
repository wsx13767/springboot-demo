package com.evolutivelabs.app.counter.api.schedule;

import com.evolutivelabs.app.counter.api.service.ExcelFileService;
import com.evolutivelabs.app.counter.common.config.redis.RedisKey;
import com.evolutivelabs.app.counter.common.exception.NotFoundException;
import com.evolutivelabs.app.counter.common.model.excelpaser.FilePath;
import com.evolutivelabs.app.counter.common.service.RedisClientService;
import com.evolutivelabs.app.counter.common.utils.PathDirUtils;
import com.evolutivelabs.app.counter.database.mysql.entity.ExcelFile;
import com.evolutivelabs.app.counter.database.mysql.repository.ExcelFileRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * excel目錄搜尋排程
 */
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class FileFindSchedule {
    private static final Logger logger = LoggerFactory.getLogger(FileFindSchedule.class);
    private final ExcelFileService excelFileService;


    /**
     * 固定排程，搜尋所有目錄並放置cache
     */
    @Scheduled(fixedRate = 120000)
    void searchFile() {
        excelFileService.saveExcelAllPathToRedis();
    }
}

