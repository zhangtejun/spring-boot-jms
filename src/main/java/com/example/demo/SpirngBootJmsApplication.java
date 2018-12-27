package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@EnableSwagger2 //表示开启Swagger2
@SpringBootApplication
public class SpirngBootJmsApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpirngBootJmsApplication.class, args);
    }
}
