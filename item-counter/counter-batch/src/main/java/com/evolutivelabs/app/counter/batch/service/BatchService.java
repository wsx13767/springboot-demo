package com.evolutivelabs.app.counter.batch.service;

import com.evolutivelabs.app.counter.batch.annotation.BeanInject;
import com.evolutivelabs.app.counter.batch.job.CommonJob;
import com.evolutivelabs.app.counter.batch.model.ScheduledTask;
import com.evolutivelabs.app.counter.common.exception.CustomException;
import com.evolutivelabs.app.counter.common.exception.NotFoundException;
import com.evolutivelabs.app.counter.common.model.batch.BatchModel;
import com.evolutivelabs.app.counter.common.utils.CronUtils;
import com.evolutivelabs.app.counter.database.mysql.RepositoryFactory;
import com.evolutivelabs.app.counter.database.mysql.entity.BatchSchedule;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.lang.reflect.Field;
import java.util.*;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.stream.Collectors;

/**
 * 驅動排程的service
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class BatchService {
    private static final Logger logger = LoggerFactory.getLogger(BatchService.class);

    private final ApplicationContext ctx;
    private final TaskScheduler taskScheduler;
    private final RepositoryFactory repositoryFactory;

    private ReadWriteLock lock = new ReentrantReadWriteLock();
    private Lock readLock = lock.readLock();
    private Lock writeLock = lock.writeLock();

    @Value("${evolutivelabs.batch.system}")
    private String system;

    private Map<String, ScheduledFuture<?>> jobsMap = new HashMap<>();

    /**
     * 當spring 啟動後，讀出資料庫的排程
     */
    @PostConstruct
    private void init() {
        Iterable<BatchSchedule> batchSchedules = repositoryFactory.getBatchScheduleRepository().findAllByStatusAndSystem(true, system);
        for (BatchSchedule batchSchedule : batchSchedules) {
            try {
                getJob(batchSchedule).ifPresent(job -> {
                    putToJobsMap(batchSchedule, job);
                });
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
            }
        }
    }

    public List<BatchModel> getAllBatchJobDetail() {
        try {
            readLock.lock();
            return repositoryFactory.getBatchScheduleRepository().findBySystem(system)
                    .stream().map(batchSchedule -> {
                        BatchModel model = convertToModel(batchSchedule);
                        model.setIsOn(jobsMap.containsKey(batchSchedule.getTaskId()));
                        return model;
                    }).collect(Collectors.toList());
        } finally {
            readLock.unlock();
        }
    }

    public BatchModel getBatchModel(String taskId) {
        try {
            readLock.lock();
            BatchSchedule batchSchedule = repositoryFactory.getBatchScheduleRepository().findById(taskId)
                    .orElseThrow(() -> new NotFoundException("無此task id : " + taskId));
            BatchModel model = convertToModel(batchSchedule);
            model.setIsOn(jobsMap.containsKey(batchSchedule.getTaskId()));
            return model;
        } finally {
            readLock.unlock();
        }
    }

    /**
     * 轉換成前端的view
     *
     * @param batchSchedule
     * @return
     */
    private BatchModel convertToModel(BatchSchedule batchSchedule) {
        BatchModel batchModel = new BatchModel();
        batchModel.setTaskId(batchSchedule.getTaskId());
        batchModel.setName(batchSchedule.getName());
        batchModel.setStatus(batchSchedule.getStatus());
        batchModel.setDescription(batchSchedule.getDescription());
        batchModel.setSystem(batchSchedule.getSystem());
        batchModel.setScheduledTime(CronUtils.paserCron(batchSchedule.getCronExpression()));
        return batchModel;
    }

    /**
     * 排程放置
     *
     * @param batchSchedule
     * @param job
     */
    private synchronized void putToJobsMap(BatchSchedule batchSchedule, CommonJob job) {
        if (jobsMap.get(batchSchedule.getTaskId()) != null) return;

        ScheduledTask scheduledTask = new ScheduledTask(job);

        ScheduledFuture<?> scheduledFuture = taskScheduler.schedule(job,
                new CronTrigger(batchSchedule.getCronExpression(),
                        TimeZone.getTimeZone(TimeZone.getDefault().getID())));

        scheduledTask.setFuture(scheduledFuture);

        jobsMap.put(batchSchedule.getTaskId(), scheduledFuture);
    }

    /**
     * 取得排程資訊
     *
     * @param batchSchedule
     * @return
     * @throws Exception
     */
    private Optional<CommonJob> getJob(BatchSchedule batchSchedule) {
        try {
            Class<?> clazz = Class.forName(batchSchedule.getClassName());
            if (!CommonJob.class.isAssignableFrom(clazz)) return Optional.empty();

            Object obj = clazz.getDeclaredConstructor().newInstance();
            for (Field field : obj.getClass().getDeclaredFields()) {
                field.setAccessible(true);
                BeanInject beanInject = field.getAnnotation(BeanInject.class);
                if (beanInject == null) continue;
                field.set(obj, ctx.getBean(field.getType()));
            }
            return Optional.of((CommonJob) obj);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    /**
     * 啟動排程
     *
     * @param taskId
     * @throws Exception
     */
    public synchronized BatchModel startTask(String taskId) {
        try {
            writeLock.lock();
            if (jobsMap.get(taskId) != null) {
                throw new NotFoundException("排程已啟動: " + taskId);
            }

            BatchSchedule batchSchedule = repositoryFactory.getBatchScheduleRepository()
                    .findBy(taskId, false, system)
                    .orElseThrow(() -> new NotFoundException("排程不存在: " + taskId));


            CommonJob job = getJob(batchSchedule).orElseThrow(() -> new NotFoundException("排程生成失敗: " + taskId));

            putToJobsMap(batchSchedule, job);
            batchSchedule.setStatus(true);
            repositoryFactory.getBatchScheduleRepository().save(batchSchedule);
            BatchModel model = convertToModel(batchSchedule);
            model.setIsOn(jobsMap.containsKey(taskId));

            return model;
        } finally {
            writeLock.unlock();
        }
    }

    /**
     * 關閉排程
     *
     * @param taskId
     */
    public synchronized BatchModel stopTask(String taskId) {
        try {
            writeLock.lock();
            if (jobsMap.get(taskId) == null) {
                throw new NotFoundException("排程已關閉: " + taskId);
            }

            BatchSchedule batchSchedule = repositoryFactory.getBatchScheduleRepository().findBy(taskId, true, system)
                    .orElseThrow(() -> new NotFoundException("排程不存在: " + taskId));

            ScheduledFuture<?> scheduledFuture = jobsMap.get(taskId);
            if (scheduledFuture != null) {
                if (scheduledFuture.cancel(true)) {
                    jobsMap.remove(taskId);

                    batchSchedule.setStatus(false);
                    repositoryFactory.getBatchScheduleRepository().save(batchSchedule);
                }
            }
            BatchModel model = convertToModel(batchSchedule);
            model.setIsOn(jobsMap.containsKey(taskId));
            return model;
        } finally {
            writeLock.unlock();
        }
    }

    @PreDestroy
    private void destroy() {
        jobsMap.values().forEach(furture -> furture.cancel(true));
    }

}
