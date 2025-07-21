package com.example.bootredis.controller;

import com.example.bootredis.domain.User;
import com.example.bootredis.repository.UserRepository;
import com.example.bootredis.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/users/{id}")
    public User getUser(@PathVariable Long id) {
        // 사용자 ID로 사용자 정보를 조회
        return userService.getUserById(id);
    }


}
