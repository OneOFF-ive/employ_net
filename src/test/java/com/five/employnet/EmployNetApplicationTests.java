package com.five.employnet;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.five.employnet.common.JwtUtil;
import com.five.employnet.entity.User;
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
    @Test
    void contextLoads() {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getOpenId, "oX6_d5fOfDXSO1PsGYRpbBS7sXOo");
        User user = userService.getOne(queryWrapper);
        Long id = user.getId();
        String token = jwtUtil.generateToken(String.valueOf(id));
        log.info(token);
    }

}
