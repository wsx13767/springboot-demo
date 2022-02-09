package com.evolutivelabs.app.counter.common.service;

import com.evolutivelabs.app.counter.common.config.redis.RedisKey;

import java.util.Map;

public interface RedisClientService {
    boolean expire(RedisKey key, long time);
    long getExpire(RedisKey key);
    boolean exists(RedisKey key);
    void del(String... key);
    boolean set(RedisKey key, Object value, long time);
    boolean set(RedisKey key, Object value);
    <T> T get(RedisKey key);
}
