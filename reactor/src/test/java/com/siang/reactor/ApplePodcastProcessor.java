package com.siang.reactor;

import java.util.concurrent.Flow;
import java.util.concurrent.Flow.*;
import java.util.concurrent.SubmissionPublisher;
import java.util.function.Function;

public class ApplePodcastProcessor<T, R> extends SubmissionPublisher<R> implements Processor<T, R> {
    private Subscription subscription;
    private Function<T, R> function;

    public ApplePodcastProcessor(Function<T, R> function) {
        super();
        this.function = function;
    }

    @Override
    public void onSubscribe(Flow.Subscription subscription) {
        this.subscription = subscription;
        this.subscription.request(1);
    }

    @Override
    public void onNext(T item) {
        submit(function.apply(item));
        subscription.request(1);
    }

    @Override
    public void onError(Throwable throwable) {
        System.out.println("onError:" + throwable);
    }

    @Override
    public void onComplete() {
        System.out.println("onComplete");
    }
}
