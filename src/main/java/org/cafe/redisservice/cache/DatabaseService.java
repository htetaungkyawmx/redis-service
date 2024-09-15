package org.cafe.redisservice.cache;

public interface DatabaseService {
    Object fetchData(String key);
    Object saveData(String key, Object value);
    void deleteData(String key);
}
