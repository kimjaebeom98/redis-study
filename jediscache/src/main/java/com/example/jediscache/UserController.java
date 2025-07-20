package com.example.jediscache;

import com.example.jediscache.config.JedisConfig;
import com.example.jediscache.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserController {
    private final UserRepository userRepository;
    private final JedisConfig jedisConfig;

    @GetMapping("/users/{id}/email")
    public String getUserEmail(@PathVariable Long id) {
        // Redis에서 이메일을 조회하는 로직
        try (var jedis = jedisConfig.createJedisPool().getResource()) {
            // 1. Redis에서 이메일 조회
            String email = jedis.get("user:%d:email".formatted(id));
            // Cache hit: Redis에서 이메일을 찾았는지 확인
            if (email != null) {
                System.out.println("Cache hit: " + email);
                return email;
            // 2. Redis에 없으면 DB에서  이메일 조회
            } else {
                System.out.println("Cache miss for id: " + id);
                String userEmail = userRepository.findById(id)
                        .map(user -> user.getEmail())
                        .orElse("User not found");
                // 3. 조회한 이메일을 Redis에 저장
                jedis.set("user:%d:email".formatted(id), userEmail);
                return userEmail;
            }
        }
    }
}
