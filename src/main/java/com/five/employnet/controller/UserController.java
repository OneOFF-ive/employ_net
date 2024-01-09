package com.five.employnet.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.five.employnet.common.BaseContext;
import com.five.employnet.common.JwtUtil;
import com.five.employnet.common.R;
import com.five.employnet.common.ValidateCodeUtils;
import com.five.employnet.dto.UserDto;
import com.five.employnet.entity.*;
import com.five.employnet.service.*;
import com.five.employnet.view.JobView;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.beans.BeanUtils;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

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
    private final JobViewService jobViewService;

    private final RedisTemplate<Object, Object> redisTemplate;

    public UserController(UserService userService, WeChatService weChatService, JwtUtil jwtUtil, JobCollectionService jobCollectionService, JobService jobService, TalentCollectionService talentCollectionService, TalentService talentService, CompanyService companyService, JobViewService jobViewService, RedisTemplate<Object, Object> redisTemplate) {
        this.userService = userService;
        this.weChatService = weChatService;
        this.jwtUtil = jwtUtil;
        this.jobCollectionService = jobCollectionService;
        this.jobService = jobService;
        this.talentCollectionService = talentCollectionService;
        this.talentService = talentService;
        this.companyService = companyService;
        this.jobViewService = jobViewService;
        this.redisTemplate = redisTemplate;
    }

    @GetMapping("/auth")
    public R<String> auth() {
        return R.success("OK");
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
    public R<UserDto> login(HttpServletRequest request, @RequestBody Map<String, String> requestBody) {

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
        } else {
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
            } else return R.success("转移失败");
        } else return R.error("用户不存在");
    }

    //查询用户收藏的职位
    @GetMapping("/job_collection")
    public R<List<JobView>> getUserCollection() {
        String userId = BaseContext.getCurrentId();

        LambdaQueryWrapper<JobCollection> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(JobCollection::getUser_id, userId);
        List<JobCollection> jobCollections = jobCollectionService.list(queryWrapper);
        List<String> jobIdList = jobCollections.stream().map(
                JobCollection::getJob_id
        ).toList();
        List<JobView> resInfo = new ArrayList<>();
        if (!jobIdList.isEmpty()) {
            resInfo = jobViewService.listByIds(jobIdList);
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
            for (Talent talent : talentList) {
                talentService.completeTalent(talent);
            }
            return R.success(talentList);
        } else return R.success(null);
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

    @GetMapping("/getUserId")
    public R<String> getUserId(@RequestParam("companyId") String companyId) {
        Company company = companyService.getById(companyId);
        return R.success(company.getUser_id());
    }

    @GetMapping("/all")
    public R<Page<UserDto>> allUsers(@RequestParam int page, @RequestParam int pageSize) {
        Page<UserDto> userDtoPage = new Page<>(page, pageSize);
        Page<User> userPage = new Page<>(page, pageSize);
        userService.page(userPage);
        List<User> userList = userPage.getRecords();
        List<UserDto> userDtoList = userList.stream().map((user -> {
            UserDto userDto = new UserDto();
            BeanUtils.copyProperties(user, userDto);
            return userDto;
        })).toList();
        userDtoPage.setRecords(userDtoList);
        return R.success(userDtoPage);
    }

    @GetMapping("/id")
    public R<UserDto> user(@RequestParam("id") String id) {
        User user = userService.getById(id);
        if (user != null) {
            UserDto userDto = new UserDto();
            BeanUtils.copyProperties(user, userDto);
            return R.success(userDto);
        }
        return R.error("用户不存在");
    }

    @GetMapping("/count")
    public R<Integer> count() {
        int count = userService.count();
        return R.success(count);
    }

    @PostMapping("/send/code")
    public R<String> sendCode(@RequestBody User user) {
        String phone = user.getPhone_number();
        if (!phone.isEmpty()) {
            String code = ValidateCodeUtils.generateValidateCode(4).toString();
            log.info("code = " + code);
//            SMSUtils.sendMessage("阿里云短信测试", "SMS_154950909", phone, code);
            redisTemplate.opsForValue().set(phone, code, 5, TimeUnit.MINUTES);
            return R.success("手机验证码短信发送成功");
        }
        return R.error("手机验证码短信发送失败");
    }

    @PostMapping("/verify/code")
    public R<UserDto> verifyCode(@RequestBody Map<String, String> req) {
        String phone = req.get("phone_number");
        String code = req.get("code");

        String codeInRedis = (String) redisTemplate.opsForValue().get(phone);

        if (codeInRedis != null && codeInRedis.equals(code)) {
            LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(User::getPhone_number, phone);
            User user = userService.getOne(queryWrapper);
            if (user == null) {
                user = new User();
                user.setPhone_number(phone);
                userService.save(user);
            }
            UserDto userDto = new UserDto();
            BeanUtils.copyProperties(user, userDto);
            R<UserDto> res = R.success(userDto);

            String userId = user.getUser_id();
            String token = jwtUtil.generateToken(userId);
            res.add("token", token);

            LambdaQueryWrapper<Company> companyQueryWrapper = new LambdaQueryWrapper<>();
            Company company = companyService.getCompanyByUserId(userId);
            if (company != null) {
                res.add("company", company);
            }

            LambdaQueryWrapper<Talent> talentQueryWrapper = new LambdaQueryWrapper<>();
            Talent talent = talentService.getTalentByUserId(userId);
            if (talent != null) {
                res.add("talent", talent);
            }
            return res;
        }
        return R.error("验证失败");
    }
}


