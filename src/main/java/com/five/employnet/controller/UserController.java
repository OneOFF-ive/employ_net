package com.five.employnet.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.five.employnet.common.BaseContext;
import com.five.employnet.common.JwtUtil;
import com.five.employnet.common.R;
import com.five.employnet.dto.UserDto;
import com.five.employnet.entity.*;
import com.five.employnet.service.*;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.beans.BeanUtils;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Slf4j
@RestController
@RequestMapping("/user")
public class UserController {
    private final UserService userService;
    private final WeChatService weChatService;
    private final JwtUtil jwtUtil;
    private final JobCollectionService jobCollectionService;
    private final JobService jobService;
    private final TalentCollectionService talentCollectionService;
    private final TalentService talentService;
    private final CompanyService companyService;

    public UserController(UserService userService, WeChatService weChatService, JwtUtil jwtUtil, JobCollectionService jobCollectionService, JobService jobService, TalentCollectionService talentCollectionService, TalentService talentService, CompanyService companyService) {
        this.userService = userService;
        this.weChatService = weChatService;
        this.jwtUtil = jwtUtil;
        this.jobCollectionService = jobCollectionService;
        this.jobService = jobService;
        this.talentCollectionService = talentCollectionService;
        this.talentService = talentService;
        this.companyService = companyService;
    }

    @PostMapping("/update")
    public R<UserDto> update(@RequestBody User user) {
        String userId = BaseContext.getCurrentId();
        user.setUser_id(userId);
        UpdateWrapper<User> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("user_id", userId);
        userService.update(user, updateWrapper);
        UserDto userDto = new UserDto();
        BeanUtils.copyProperties(user, userDto);
        userDto.setUser_id(userId);
        return R.success(userDto);
    }

    @PostMapping("/login")
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

            LambdaQueryWrapper<Company> companyLambdaQueryWrapper = new LambdaQueryWrapper<>();
            companyLambdaQueryWrapper.eq(Company::getUser_id, userId);
            Company company = companyService.getOne(companyLambdaQueryWrapper);
            if (company != null) {
                res.add("company", company);
            }
            return res;
        }
    }

    @PostMapping("/login/username")
    public R<UserDto> login(@RequestBody User user) {
        String password = DigestUtils.md5DigestAsHex(user.getPassword().getBytes());
        String username = user.getUsername();
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getUsername, username);
        User u = userService.getOne(queryWrapper);
        if (u == null) {
            return R.error("用户不存在");
        } else if (!Objects.equals(u.getPassword(), password)) {
            return R.error("密码错误");
        } else {
            UserDto userDto = new UserDto();
            BeanUtils.copyProperties(u, userDto);
            String userId = u.getUser_id();
            String token = jwtUtil.generateToken(String.valueOf(userId));
            return R.success(userDto).add("token", token);
        }
    }

    @PostMapping("/setUsername")
    public R<String> setUsername(@RequestBody User user) {
        String userId = BaseContext.getCurrentId();
        String username = user.getUsername();
        String password = DigestUtils.md5DigestAsHex(user.getPassword().getBytes());
        user.setUser_id(userId);
        user.setPassword(password);

        LambdaQueryWrapper<User> userLambdaQueryWrapper = new LambdaQueryWrapper<>();
        userLambdaQueryWrapper.eq(User::getUsername, username);
        User u = userService.getOne(userLambdaQueryWrapper);
        if (u == null || Objects.equals(u.getUser_id(), userId)) {
            UpdateWrapper<User> updateWrapper = new UpdateWrapper<>();
            updateWrapper.eq("user_id", user.getUser_id());
            userService.update(user, updateWrapper);
            return R.success("设置成功");
        } else {
            return R.error("用户名已存在");
        }
    }

    @PostMapping("/verifyPassword")
    public R<String> verifyPassword(@RequestBody Map<String, String> requestBody) {
        String userId = BaseContext.getCurrentId();
        String password = DigestUtils.md5DigestAsHex(requestBody.get("password").getBytes());
        User user = userService.getById(userId);
        if (user == null || !Objects.equals(user.getPassword(), password)) {
            return R.error("密码错误");
        }
        else {
            return R.success("密码正确");
        }
    }

    @PostMapping("/transfer")
    public R<String> transfer(@RequestBody Map<String, String> requestBody) {
        String userId = BaseContext.getCurrentId();
        User currentUser = userService.getById(userId);

        String username = requestBody.get("username");
        LambdaQueryWrapper<User> userLambdaQueryWrapper = new LambdaQueryWrapper<>();
        userLambdaQueryWrapper.eq(User::getUsername, username);
        User targetUser = userService.getOne(userLambdaQueryWrapper);
        if (targetUser != null) {
            if (userService.transfer(currentUser, targetUser)) {
                return R.success("转移成功");
            }
            else return R.success("转移失败");
        } else return R.error("用户不存在");
    }

    //查询用户收藏的职位
    @GetMapping("/job_collection")
    public R<List<Job>> getUserCollection() {
        String userId = BaseContext.getCurrentId();

        LambdaQueryWrapper<JobCollection> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(JobCollection::getUser_id, userId);
        List<JobCollection> jobCollections = jobCollectionService.list(queryWrapper);
        List<String> jobIdList = jobCollections.stream().map(
                JobCollection::getJob_id
        ).toList();
        List<Job> resInfo = new ArrayList<>();
        if (!jobIdList.isEmpty()) {
            resInfo = jobService.listByIds(jobIdList);
        }
        return R.success(resInfo);
    }

    //用户收藏一个职位
    @PostMapping("/collect_job")
    public R<String> collectJob(@RequestBody Map<String, String> requestBody) {
        try {
            String userId = BaseContext.getCurrentId();
            String jobId = requestBody.get("job_id");
            JobCollection jobCollection = new JobCollection();
            jobCollection.setUser_id(userId);
            jobCollection.setJob_id(jobId);
            jobCollectionService.save(jobCollection);
        } catch (DataIntegrityViolationException e) {
            return R.error("收藏失败");
        }
        return R.success("收藏成功");
    }

    //用户删除一个收藏的职位
    @DeleteMapping("/delete_job")
    public R<String> deleteJob(@RequestParam("job_id") String jobId) {
        try {
            String userId = BaseContext.getCurrentId();

            LambdaQueryWrapper<JobCollection> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper
                    .eq(JobCollection::getUser_id, userId)
                    .eq(JobCollection::getJob_id, jobId);
            jobCollectionService.remove(queryWrapper);
        } catch (Exception e) {
            return R.error("删除失败");
        }
        return R.success("删除成功");
    }

    //用户收藏一个人才
    @PostMapping("/collect_talent")
    public R<String> collectTalent(@RequestBody Map<String, String> requestBody) {
        try {
            String talentId = requestBody.get("talent_id");
            String userId = BaseContext.getCurrentId();
            TalentCollection talentCollection = new TalentCollection();
            talentCollection.setTalent_id(talentId);
            talentCollection.setUser_id(userId);
            talentCollectionService.save(talentCollection);
            return R.success("收藏成功");
        } catch (Exception e) {
            return R.error("收藏失败");
        }
    }

    //查询人才收藏库
    @GetMapping("talent_collection")
    public R<List<Talent>> getTalentCollection() {
        String userId = BaseContext.getCurrentId();
        LambdaQueryWrapper<TalentCollection> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(TalentCollection::getUser_id, userId);
        List<TalentCollection> talentCollectionList = talentCollectionService.list(queryWrapper);
        if (!talentCollectionList.isEmpty()) {
            List<String> talentIdList = talentCollectionList.stream().map(
                    TalentCollection::getTalent_id
            ).toList();
            List<Talent> talentList = talentService.listByIds(talentIdList);
            for (Talent talent:talentList) {
                talentService.completeTalent(talent);
            }
            return R.success(talentList);
        }
        else return R.success(null);
    }

    //用户取消人才收藏
    @DeleteMapping("delete_talent")
    public R<String> deleteTalent(@RequestParam("talent_id") String talentId) {
        String useId = BaseContext.getCurrentId();
        LambdaQueryWrapper<TalentCollection> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper
                .eq(TalentCollection::getTalent_id, talentId)
                .eq(TalentCollection::getUser_id, useId);
        talentCollectionService.remove(queryWrapper);
        return R.success("删除成功");
    }

}


