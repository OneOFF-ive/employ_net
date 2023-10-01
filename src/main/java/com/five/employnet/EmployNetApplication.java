package com.five.employnet;

import org.apache.tomcat.util.descriptor.web.SecurityCollection;
import org.apache.tomcat.util.descriptor.web.SecurityConstraint;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.naming.Context;

@SpringBootApplication
@MapperScan("com.five.employnet.mapper")
@EnableTransactionManagement
@EnableCaching
public class EmployNetApplication {

    public static void main(String[] args) {
        SpringApplication.run(EmployNetApplication.class, args);
    }

}
