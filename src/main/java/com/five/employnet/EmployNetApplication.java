package com.five.employnet;


import com.five.employnet.service.VisitorService;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.annotation.PostConstruct;


@SpringBootApplication
@MapperScan("com.five.employnet.mapper")
@EnableTransactionManagement
@EnableCaching
public class EmployNetApplication {

    public EmployNetApplication(VisitorService visitorService) {
        this.visitorService = visitorService;
    }

    public static void main(String[] args) {
        ConfigurableApplicationContext applicationContext = SpringApplication.run(EmployNetApplication.class, args);
    }

    final VisitorService visitorService;

    @PostConstruct
    public void init() {
        visitorService.scheduleSaveVisitorRecordsToDatabase();
    }
}
