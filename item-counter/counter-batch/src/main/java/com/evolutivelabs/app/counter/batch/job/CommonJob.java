package com.evolutivelabs.app.counter.batch.job;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ScheduledFuture;

public abstract class CommonJob implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(CommonJob.class);

    @Override
    public void run() {
        exec();
    }

    private void exec() {
        try {
            logger.info("========================={} execute()========================", this.getClass());
            execute();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        } finally {
            logger.info("========================={} end()========================", this.getClass());
        }
    }

    protected abstract void execute() throws Exception;
}
