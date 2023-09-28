package com.five.employnet.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.five.employnet.common.BaseContext;
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
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.lang.invoke.LambdaMetafactory;
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
//        String authorizationHeader = request.getHeader("Authorization");
//        String authToken = authorizationHeader.substring(7); // 去掉"Bearer "前缀
//        String userId = jwtUtil.extractUserId(authToken);

        String userId = BaseContext.getCurrentId();
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

    @GetMapping("/job_collection")
    public R<List<Job>> getUserCollection(HttpServletRequest request) {
//        String authorizationHeader = request.getHeader("Authorization");
//        String authToken = authorizationHeader.substring(7); // 去掉"Bearer "前缀
//        String userId = jwtUtil.extractUserId(authToken);
        String userId = BaseContext.getCurrentId();

        LambdaQueryWrapper<UserCollection> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserCollection::getUser_id, userId);
        List<UserCollection> userCollections = userCollectionService.list(queryWrapper);
        List<String> jobIdList = userCollections.stream().map(
                UserCollection::getJob_id
        ).toList();
        List<Job> resInfo = new ArrayList<>();
        if (!jobIdList.isEmpty()) {
            resInfo = jobService.listByIds(jobIdList);
            for (Job job : resInfo) {
                jobService.completeJob(job);
            }
        }
        return R.success(resInfo);
    }

    @PostMapping("collect_job")
    public R<String> collectJob(HttpServletRequest request, @RequestBody Map<String, String> requestBody) {
        try {
//            String authorizationHeader = request.getHeader("Authorization");
//            String authToken = authorizationHeader.substring(7); // 去掉"Bearer "前缀
//            String userId = jwtUtil.extractUserId(authToken);
            String userId = BaseContext.getCurrentId();
            String jobId = requestBody.get("job_id");
            UserCollection userCollection = new UserCollection();
            userCollection.setUser_id(userId);
            userCollection.setJob_id(jobId);
            userCollectionService.save(userCollection);
        } catch (DataIntegrityViolationException e) {
            return R.error("收藏失败");
        }
        return R.success("收藏成功");
    }

    @DeleteMapping("delete_job")
    public R<String> deleteJob(HttpServletRequest request, @RequestBody Map<String, String> requestBody) {
        try {
//            String authorizationHeader = request.getHeader("Authorization");
//            String authToken = authorizationHeader.substring(7); // 去掉"Bearer "前缀
//            String userId = jwtUtil.extractUserId(authToken);
            String userId = BaseContext.getCurrentId();
            String jobId = requestBody.get("job_id");

            LambdaQueryWrapper<UserCollection> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper
                    .eq(UserCollection::getUser_id, userId)
                    .eq(UserCollection::getJob_id, jobId);
            userCollectionService.remove(queryWrapper);
        } catch (Exception e) {
            return R.error("删除失败");
        }
        return R.error("删除成功");
    }

}


