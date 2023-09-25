package com.five.employnet;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@MapperScan("com.five.employnet.mapper")
@EnableTransactionManagement
@EnableCaching
public class EmployNetApplication {

    public static void main(String[] args) {
        SpringApplication.run(EmployNetApplication.class, args);
    }

}
