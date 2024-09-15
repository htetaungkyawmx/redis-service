package org.cafe.redisservice.cache;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class CacheServiceImpl implements CacheService{

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    private ValueOperations<String, Object> valueOperations;

    @Autowired
    private DatabaseService databaseService;

    @PostConstruct
    private void  init() {
        valueOperations = redisTemplate.opsForValue();
    }

    public void set(String key, Object value, int ttl, TimeUnit timeUnit) {
        valueOperations.set(key, value, ttl, timeUnit);
    }

    public void set(String key, Object value) {
        valueOperations.set(key, value);
    }

    public Object get(String key) {
        return valueOperations.get(key);
    }

    @Override
    public void delete(String key) {
        redisTemplate.delete(key);
    }

    @Override
    public void write(String key, Object value) {
        set(key, value);
       writeToDBAsync(key, value);
    }

    @Override
    public void write(String key, Object value, int ttl, TimeUnit timeUnit) {
        set(key, value, ttl, timeUnit);
        writeToDBAsync(key, value);
    }

    @Async
    protected void writeToDBAsync(String key, Object value) {
        databaseService.saveData(key, value);
    }

    @Override
    public Object read(String key, int ttl, TimeUnit timeUnit) {
       Object value = get(key);
       if (value == null) {
           value = databaseService.fetchData(key);
           if (value != null) {
               set(key, value, ttl, timeUnit);
           }
       }
       return value;
    }
}
