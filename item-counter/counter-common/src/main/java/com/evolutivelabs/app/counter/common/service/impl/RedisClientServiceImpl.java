package com.evolutivelabs.app.counter.common.service.impl;

import com.evolutivelabs.app.counter.common.config.properties.EvolutivelabsProperties;
import com.evolutivelabs.app.counter.common.config.redis.RedisKey;
import com.evolutivelabs.app.counter.common.service.RedisClientService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class RedisClientServiceImpl implements RedisClientService {
    private final EvolutivelabsProperties evolutivelabsProperties;
    private final RedisTemplate redisTemplate;

    @Override
    public boolean expire(RedisKey key, long time) {
        return redisTemplate.expire(realKey(key), time, TimeUnit.SECONDS);
    }

    @Override
    public long getExpire(RedisKey key) {
        return redisTemplate.getExpire(realKey(key));
    }

    @Override
    public boolean exists(RedisKey key) {
        return redisTemplate.hasKey(realKey(key));
    }

    @Override
    public void del(String... keys) {
        Set<String> allKey = Arrays.stream(keys).map(key -> redisTemplate.keys(realKey(key)))
                .reduce(new HashSet(), (v1, v2) -> {
                    v1.addAll(v2);
                    return v1;
                });
        redisTemplate.delete(allKey);
    }

    @Override
    public boolean set(RedisKey key, Object value, long time) {
        try {
            redisTemplate.opsForValue().set(realKey(key), value, time, TimeUnit.SECONDS);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    @Override
    public boolean set(RedisKey key, Object value) {
        try {
            redisTemplate.opsForValue().set(realKey(key), value);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    @Override
    public <T> T get(RedisKey key) {
        return key == null ? null : (T) redisTemplate.opsForValue().get(realKey(key));
    }


    private String realKey(RedisKey key) {
        return this.realKey(key.name());
    }

    private String realKey(String key) {
        return evolutivelabsProperties.getCONTEXT_PATH().concat("_").concat(key);
    }
}
