package com.five.employnet.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.five.employnet.common.JwtUtil;
import com.five.employnet.common.R;
import com.five.employnet.common.WeChatResponse;
import com.five.employnet.entity.User;
import com.five.employnet.service.UserService;
import com.five.employnet.service.WeChatService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController("/user")
public class UserController {
    private final UserService userService;
    private final WeChatService weChatService;
    private final JwtUtil jwtUtil;

    public UserController(UserService userService, WeChatService weChatService, JwtUtil jwtUtil) {
        this.userService = userService;
        this.weChatService = weChatService;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/register")
    public Object register(@RequestParam("code") String code,
                           @RequestBody User user) {

        WeChatResponse weChatResponse = weChatService.getWeChatSessionInfo(code);
        if (weChatResponse.getErrCode() == 0) {
            String openId = weChatResponse.getOpenId();
            String sessionKey = weChatResponse.getSessionKey();
            user.setOpenId(openId);
            user.setSessionKey(sessionKey);
            LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(User::getOpenId, openId);
            User u = userService.getOne(queryWrapper);
            if (u == null) {
                userService.save(user);
                String token = jwtUtil.generateToken(openId);
                return R.success(token);
            } else {
                return R.error("用户已存在");
            }
        } else {
            return R.error("微信授权失败");
        }
    }
}


