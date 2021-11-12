package com.siang.reactor;


import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Flow;
import java.util.concurrent.Flow.*;

public class Member<T> implements Subscriber<T> {
    private Subscription subscription;
    private List<T> episodes = new LinkedList<>();

    public Subscription getSubscription() {
        return subscription;
    }

    public void setSubscription(Subscription subscription) {
        this.subscription = subscription;
    }

    public List<T> getEpisodes() {
        return episodes;
    }

    public void setEpisodes(List<T> episodes) {
        this.episodes = episodes;
    }

    @Override
    public void onSubscribe(Flow.Subscription subscription) {
        this.subscription = subscription;
        this.subscription.request(1);
    }

    @Override
    public void onNext(T item) {
        System.out.println("onNext:" + item);
        episodes.add(item);
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
