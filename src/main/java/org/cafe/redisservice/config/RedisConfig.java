package org.cafe.redisservice.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisClientConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.text.SimpleDateFormat;
import java.time.Duration;

@Configuration
public class RedisConfig {

    @Value("${redis.host}")
    private String REDIS_HOST;

    @Value("${redis.port}")
    private int REDIS_PORT;

    @Value("${redis.password}")
    private String REDIS_PASSWORD;

    @Value("${redis.max-connection}")
    private int MAX_CON;

    @Value("${redis.idle-connection}")
    private int IDLE_CON;

    @Value("${redis.connect-timeout}")
    private int CONNECT_TIMEOUT;

    @Value("${redis.read-timeout}")
    private int READ_TIMEOUT;

    @Bean
    public JedisConnectionFactory jedisConnectionFactory() {
        System.out.println("REDIS_HOST = " + REDIS_HOST);
        System.out.println("REDIS_PORT = " + REDIS_PORT);
        RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration();
        redisStandaloneConfiguration.setHostName(REDIS_HOST);
        redisStandaloneConfiguration.setPort(REDIS_PORT);
        redisStandaloneConfiguration.setPassword(REDIS_PASSWORD);

        JedisClientConfiguration.JedisClientConfigurationBuilder builder = JedisClientConfiguration.builder();
        builder.connectTimeout(Duration.ofMinutes(CONNECT_TIMEOUT));
        builder.readTimeout(Duration.ofMinutes(READ_TIMEOUT));
        builder.usePooling();

        JedisConnectionFactory jedisConnectionFactory = new JedisConnectionFactory(redisStandaloneConfiguration);
        jedisConnectionFactory.getPoolConfig().setMaxTotal(MAX_CON);
        jedisConnectionFactory.getPoolConfig().setMaxIdle(IDLE_CON);

        return jedisConnectionFactory;
    }

    @Bean
    public RedisTemplate<String, Object> redisTemplate() {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(jedisConnectionFactory());

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
        Jackson2JsonRedisSerializer<ObjectMapper> jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer<>(objectMapper, ObjectMapper.class);

        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(jackson2JsonRedisSerializer);
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashValueSerializer(jackson2JsonRedisSerializer);

        return redisTemplate;
    }
}
