package com.example.bootredis.service;

import com.example.bootredis.domain.User;
import com.example.bootredis.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final RedisTemplate<String, User> redisTemplate;
    private final RedisTemplate<String, Object> genericRedisTemplate;

    public User getUserById(Long id) {
        var key = "user:%d".formatted(id);
        // Redis에서 사용자 정보를 조회
        User cachedUser = (User)genericRedisTemplate.opsForValue().get(key);
        // Cache hit: Redis에서 사용자 정보를 찾았는지 확인
        if (cachedUser != null) {
            System.out.println("Cache hit for user id: " + id);
            return cachedUser;
        }
        // Cache miss: Redis에 없으면 DB에서 사용자 정보를 조회
        System.out.println("Cache miss for user id: " + id);
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
        // 조회한 사용자 정보를 Redis에 저장
        genericRedisTemplate.opsForValue().set(key, user, Duration.ofSeconds(30)); // 30초 동안 캐시 유지
        return user;
    }

}
