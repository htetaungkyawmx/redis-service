package org.cafe.redisservice.cache;

import java.util.concurrent.TimeUnit;

public interface CacheService {
    Object read(String key, int ttl, TimeUnit timeUnit);

    void write(String key, Object value, int ttl, TimeUnit timeUnit);
    default void write(String key, Object value) {
        write(key, value);
    }

    void delete(String key);
}
