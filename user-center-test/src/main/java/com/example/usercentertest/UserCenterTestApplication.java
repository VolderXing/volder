package com.example.usercentertest;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.example.usercentertest.mapper")
public class UserCenterTestApplication {
    public static void main(String[] args) {
        SpringApplication.run(UserCenterTestApplication.class, args);
    }
}
