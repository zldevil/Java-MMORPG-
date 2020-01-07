package com.example.demoserver;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication

public class DemoserverApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemoserverApplication.class, args);
    }

}
