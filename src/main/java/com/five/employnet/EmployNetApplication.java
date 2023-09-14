package com.five.employnet;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.five.employnet")
@MapperScan("com.five.employnet.mapper")
public class EmployNetApplication {

    public static void main(String[] args) {
        SpringApplication.run(EmployNetApplication.class, args);
    }

}
