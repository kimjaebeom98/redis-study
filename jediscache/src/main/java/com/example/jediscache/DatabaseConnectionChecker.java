package com.example.jediscache;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class DatabaseConnectionChecker implements CommandLineRunner {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public void run(String... args) throws Exception {
        String dbName = jdbcTemplate.queryForObject("SELECT DATABASE()", String.class);
        System.out.println("현재 접속된 데이터베이스: " + dbName);
    }
}

