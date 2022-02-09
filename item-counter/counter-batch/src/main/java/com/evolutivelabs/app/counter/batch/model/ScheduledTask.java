package com.evolutivelabs.app.counter.batch.model;

import java.util.concurrent.ScheduledFuture;

public class ScheduledTask {
    private Runnable task;
    private ScheduledFuture<?> future;

    public ScheduledTask(Runnable task) {
        this.task = task;
    }

    public void setFuture(ScheduledFuture<?> future) {
        this.future = future;
    }

    public void cancel() {
        ScheduledFuture<?> future = this.future;
        if (future != null) {
            future.cancel(true);
        }
    }
}
