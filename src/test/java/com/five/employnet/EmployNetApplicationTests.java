package com.five.employnet;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.five.employnet.common.JwtUtil;
import com.five.employnet.entity.JobMessage;
import com.five.employnet.entity.User;
import com.five.employnet.service.JobMessageService;
import com.five.employnet.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Slf4j
class EmployNetApplicationTests {
    @Autowired
    UserService userService;
    @Autowired
    JwtUtil jwtUtil;
    @Autowired
    JobMessageService jobMessageService;

    @Test
    void contextLoads() {
        JobMessage jobMessage = new JobMessage();
        jobMessage.setJobId(1L);
        jobMessage.setDetail("写代码");
        jobMessage.setAddress("里根学院");
        jobMessage.setWelfare("没有福利");
        jobMessage.setDuty("玩的开心");
        jobMessage.setSalary("无");

        jobMessageService.save(jobMessage);
    }

}
