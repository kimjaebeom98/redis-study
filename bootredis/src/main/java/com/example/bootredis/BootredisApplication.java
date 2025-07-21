package com.example.bootredis;

import com.example.bootredis.domain.User;
import com.example.bootredis.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@RequiredArgsConstructor
@SpringBootApplication
public class BootredisApplication implements ApplicationRunner {

	private final UserRepository userRepository;

	public static void main(String[] args) {
		SpringApplication.run(BootredisApplication.class, args);
	}

	@Override
	public void run(ApplicationArguments args) throws Exception {
//		userRepository.save(User.builder()
//				.name("jaebeom1")
//				.email("jaebeom1@naver.com")
//				.build());
//
//		userRepository.save(User.builder()
//				.name("jaebeom2")
//				.email("jaebeom2@naver.com")
//				.build());
//
//		userRepository.save(User.builder()
//				.name("jaebeom3")
//				.email("jaebeom3@naver.com")
//				.build());

	}
}
