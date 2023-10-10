package com.five.employnet;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.five.employnet.common.JwtUtil;
import com.five.employnet.entity.User;
import com.five.employnet.service.JobService;
import com.five.employnet.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.DigestUtils;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
@Slf4j
class EmployNetApplicationTests {
    @Autowired
    UserService userService;
    @Autowired
    JwtUtil jwtUtil;
    @Autowired
    JobService jobService;

    @Test
    void contextLoads() {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getOpen_id, "oX6_d5fOfDXSO1PsGYRpbBS7sXOo");
        User user = userService.getOne(queryWrapper);
        log.info(user.toString());
    }

    @Test
    void test() {
        List<Integer> list = new ArrayList<>();
        list.add(1);
        log.info(list.toString());
    }
}
