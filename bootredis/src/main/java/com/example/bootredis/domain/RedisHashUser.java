package com.example.bootredis.domain;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

import java.time.LocalDateTime;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
// RedisHashUser: Redis에 해시 구조로 저장되는 사용자 엔티티
// @RedisHash: 해당 클래스가 Redis에 해시로 저장됨을 명시, value는 해시 이름, timeToLive는 TTL(초)
@RedisHash(value = "redishash-user", timeToLive = 30L) // Redis에 저장될 해시 이름과 TTL 설정
public class RedisHashUser {
    @Id
    private Long id;

    private String name;

    @Indexed // email 필드로 인덱스 생성 (검색 최적화)
    private String email;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
