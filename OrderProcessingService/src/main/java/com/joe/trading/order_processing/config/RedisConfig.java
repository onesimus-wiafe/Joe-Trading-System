package com.joe.trading.order_processing.config;

import com.joe.trading.order_processing.entities.OrderBook;
import com.joe.trading.order_processing.entities.cache.InternalOpenOrder;
import com.joe.trading.order_processing.entities.cache.MarketData;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.util.List;

@Configuration
@EnableCaching
public class RedisConfig {
    @Value(value = "${spring.data.redis.host}")
    private String host;

    @Value(value = "${spring.data.redis.port}")
    private Integer port;

    @Bean
    public LettuceConnectionFactory lettuceConnectionFactory(){
        RedisStandaloneConfiguration config = new RedisStandaloneConfiguration();
        config.setHostName(host);
        config.setPort(port);

        return new LettuceConnectionFactory(config);
    }

    @Bean
    public RedisTemplate<String, MarketData> redisTemplate(){
        RedisTemplate<String, MarketData> redisTemplate = new RedisTemplate<>();
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setConnectionFactory(this.lettuceConnectionFactory());
        redisTemplate.setValueSerializer(new Jackson2JsonRedisSerializer<>(MarketData.class));

        return redisTemplate;
    }

    @Bean
    public RedisTemplate<String, List<InternalOpenOrder>> internalOrderRedisTemplate(){
        RedisTemplate<String, List<InternalOpenOrder>> redisTemplate = new RedisTemplate<>();
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setConnectionFactory(this.lettuceConnectionFactory());
        redisTemplate.setValueSerializer(new GenericJackson2JsonRedisSerializer());

        return redisTemplate;
    }

    @Bean
    public RedisTemplate<String, List<OrderBook>> orderBookRedisTemplate(){
        RedisTemplate<String, List<OrderBook>> redisTemplate = new RedisTemplate<>();
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setConnectionFactory(this.lettuceConnectionFactory());
        redisTemplate.setValueSerializer(new GenericJackson2JsonRedisSerializer());

        return redisTemplate;
    }
}
