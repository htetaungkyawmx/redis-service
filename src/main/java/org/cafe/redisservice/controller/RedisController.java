package org.cafe.redisservice.controller;

import org.cafe.redisservice.cache.RedisCacheService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RedisController {
    @Autowired
    private RedisCacheService redisCacheService;

    @GetMapping
    public ResponseEntity<String> test() {
        redisCacheService.set("test", "test data");
        return ResponseEntity.ok("ok");
    }
}
