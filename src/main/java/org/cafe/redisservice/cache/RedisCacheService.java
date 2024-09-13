package org.cafe.redisservice.cache;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Repository;

import java.util.concurrent.TimeUnit;

@Repository
public class RedisCacheService {
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    private ValueOperations<String, Object> valueOperations;

    @PostConstruct
    private void  init() {
        valueOperations = redisTemplate.opsForValue();
    }

    public void set(String key, Object value) {
        valueOperations.set(key, value);
    }

    public void set(String key, Object value, int ttl) {
        valueOperations.set(key, value, ttl, TimeUnit.MINUTES);
    }

    public Object get(String key) {
        return valueOperations.get(key);
    }

    public void delete(String key) {
        redisTemplate.delete(key);
    }
}
