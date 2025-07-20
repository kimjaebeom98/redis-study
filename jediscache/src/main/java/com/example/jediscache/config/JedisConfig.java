package com.example.jediscache.config;

import org.springframework.stereotype.Component;
import redis.clients.jedis.JedisPool;

@Component
public class JedisConfig {
    public JedisPool createJedisPool() {
        // JedisPool 생성 로직을 여기에 작성합니다.
        // 예시로 기본 설정을 사용합니다.
        return new JedisPool("127.0.0.1", 6379);
    }
}
