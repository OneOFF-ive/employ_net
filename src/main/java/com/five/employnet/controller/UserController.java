package com.five.employnet.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.five.employnet.common.JwtUtil;
import com.five.employnet.common.R;
import com.five.employnet.dto.UserDto;
import com.five.employnet.entity.Job;
import com.five.employnet.entity.User;
import com.five.employnet.entity.UserCollection;
import com.five.employnet.service.JobService;
import com.five.employnet.service.UserCollectionService;
import com.five.employnet.service.UserService;
import com.five.employnet.service.WeChatService;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/user")
public class UserController {
    private final UserService userService;
    private final WeChatService weChatService;
    private final JwtUtil jwtUtil;
    private final UserCollectionService userCollectionService;
    private final JobService jobService;

    public UserController(UserService userService, WeChatService weChatService, JwtUtil jwtUtil, UserCollectionService userCollectionService, JobService jobService) {
        this.userService = userService;
        this.weChatService = weChatService;
        this.jwtUtil = jwtUtil;
        this.userCollectionService = userCollectionService;
        this.jobService = jobService;
    }

    @PostMapping("/update")
    public R<String> update(HttpServletRequest request, @RequestBody User user) {
        String authorizationHeader = request.getHeader("Authorization");
        String authToken = authorizationHeader.substring(7); // 去掉"Bearer "前缀
        String userId = jwtUtil.extractUserId(authToken);
        user.setUser_id(userId);
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getUser_id, userId);
        userService.updateById(user);
        return R.success("success");
    }

    @RequestMapping("/login")
    public R<UserDto> login(@RequestBody Map<String, String> requestBody) {
        String code = requestBody.get("code");

        log.info(code);

        JSONObject responseBody = weChatService.getWeChatSessionInfo(code);

        if (responseBody.has("errcode")) {
            return R.error("微信授权失败");
        } else {
            String openId = (String) responseBody.get("openid");
            String sessionKey = (String) responseBody.get("session_key");

            LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(User::getOpen_id, openId);
            User user = userService.getOne(queryWrapper);
            if (user == null) {
                user = new User();
                user.setOpen_id(openId);
                user.setSession_key(sessionKey);
                log.info(user.toString());
                userService.save(user);
            }
            UserDto userDto = new UserDto();
            BeanUtils.copyProperties(user, userDto);
            R<UserDto> res = R.success(userDto);

            String userId = user.getUser_id();
            String token = jwtUtil.generateToken(String.valueOf(userId));
            res.add("token", token);
            return res;
        }
    }

    @PostMapping("/test")
    public List<String> test(@RequestBody Map<String, String> requestBody) {
        List<String> res = new ArrayList<>();
        res.add("12");
        res.add("123");
        return res;
    }

    @GetMapping("/collection")
    public R<List<Job>> getUserCollection(HttpServletRequest request) {
        String authorizationHeader = request.getHeader("Authorization");
        String authToken = authorizationHeader.substring(7); // 去掉"Bearer "前缀
        String userId = jwtUtil.extractUserId(authToken);

        LambdaQueryWrapper<UserCollection> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserCollection::getUser_id, userId);
        List<UserCollection> userCollections = userCollectionService.list(queryWrapper);
        List<String> jobIdList = userCollections.stream().map(
                UserCollection::getJob_id
        ).toList();
        List<Job> res = jobService.listByIds(jobIdList);
        for (Job job: res) {
            jobService.completeJob(job);
        }
        return R.success(res);
    }
}


