package com.example.bootredis.config;

import com.example.bootredis.domain.User;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig {

    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        // 날짜를 ISO-8601 형식으로 직렬화 (2024-07-22T10:30:00)
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        // LocalDateTime 등 Java 8 Date/Time API 지원
        mapper.registerModule(new JavaTimeModule());
        // 알 수 없는 JSON 필드가 있어도 에러 안 나게 함
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        // JSON 출력 시 예쁘게 들여쓰기 (개발 환경에서 유용)
        mapper.enable(SerializationFeature.INDENT_OUTPUT);

        return mapper;
    }

    @Bean
    public RedisTemplate<String, User> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, User> template = new RedisTemplate<>();
        template.setConnectionFactory(redisConnectionFactory);
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new Jackson2JsonRedisSerializer<>(objectMapper(), User.class));
        return template;
    }
}
