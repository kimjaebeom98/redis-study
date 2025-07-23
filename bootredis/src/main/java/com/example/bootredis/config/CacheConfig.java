package com.example.bootredis.config;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.jsontype.BasicPolymorphicTypeValidator;
import com.fasterxml.jackson.databind.jsontype.PolymorphicTypeValidator;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.boot.autoconfigure.cache.RedisCacheManagerBuilderCustomizer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.util.List;

@EnableCaching // 스프링 캐싱 기능을 활성화함 (필수!)
@Configuration // 스프링 설정 클래스임을 명시

public class CacheConfig {

    // 캐시 이름 상수 정의 (나중에 @Cacheable("cache1") 이런 식으로 사용)
    public static final String CACHE1 = "cache1";
    public static final String CACHE2 = "cache2";

    // 캐시 이름과 TTL(초)을 묶어서 관리하기 위한 내부 클래스
    @AllArgsConstructor @Getter
    private static class CacheProperty{
        private String name;
        private Integer ttl; // Time to Live (초 단위)
    }

    // ⭐ 핵심: RedisCacheManager를 커스터마이징하는 빈을 등록
    // 스프링 부트가 RedisCacheManager를 만들 때, 이 빈을 참조해서 우리가 원하는 설정(TTL, 직렬화 등)을 적용해 줌
    // RedisConnectionFactory는 스프링 부트가 이미 자동 설정으로 등록해뒀기 때문에, 이 설정에는 명시적으로 나타나지 않음
    @Bean
    public RedisCacheManagerBuilderCustomizer redisCacheManagerBuilderCustomizer() {

        // ObjectMapper가 JSON 직렬화/역직렬화 시 허용할 타입을 정의 (여기서는 모든 객체 허용)
        PolymorphicTypeValidator typeValidator = BasicPolymorphicTypeValidator.builder()
                .allowIfSubType(Object.class)
                .build();

        // JSON 직렬화/역직렬화에 사용할 ObjectMapper 커스터마이징
        // - 모르는 속성은 무시하고
        // - 직렬화 시 클래스 타입 정보를 JSON에 포함하며 (activateDefaultTyping)
        // - Java 8 날짜/시간 API (java.time.*) 지원하고
        // - 날짜는 타임스탬프 대신 사람이 읽기 좋은 문자열 형식으로 저장
        var objectMapper = new ObjectMapper()
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                .activateDefaultTyping(typeValidator, ObjectMapper.DefaultTyping.NON_FINAL)
                .registerModule(new JavaTimeModule())
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        // 캐시별 이름과 TTL 설정 리스트
        List<CacheProperty> cacheProperties = List.of(
                new CacheProperty(CACHE1, 60),  // cache1은 60초 (1분) 만료
                new CacheProperty(CACHE2, 120) // cache2는 120초 (2분) 만료
        );

        // RedisCacheManager Builder를 커스터마이징하는 람다 표현식 반환
        // (RedisCacheManagerBuilderCustomizer 인터페이스의 customize 메소드 구현)
        return (builder -> {
            // 정의된 각 캐시 속성(이름, TTL)에 대해 RedisCacheConfiguration을 설정
            cacheProperties.forEach(cacheProperty -> {
                builder.withCacheConfiguration(cacheProperty.getName(), // 해당 캐시 이름에 대한 설정
                        RedisCacheConfiguration
                                .defaultCacheConfig()   // RedisCacheManager의 기본 설정부터 시작
                                .disableCachingNullValues() // 메서드가 null을 반환하면 캐시 저장 안 함
                                // 키(Key) 직렬화: Redis 키는 일반 문자열로 저장
                                .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer()))
                                // 값(Value) 직렬화: 위에서 커스텀한 ObjectMapper를 이용해 객체를 JSON으로 직렬화/역직렬화
                                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(new GenericJackson2JsonRedisSerializer(objectMapper)))
                                // 각 캐시별로 설정된 TTL(만료 시간) 적용
                                .entryTtl(java.time.Duration.ofSeconds(cacheProperty.getTtl())));
            });
        });
    }
}
