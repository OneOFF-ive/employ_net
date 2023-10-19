package com.five.employnet.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.fasterxml.jackson.databind.ser.Serializers;
import com.five.employnet.common.BaseContext;
import com.five.employnet.common.JwtUtil;
import com.five.employnet.common.R;
import com.five.employnet.config.CorsConfig;
import com.five.employnet.dto.AdminDto;
import com.five.employnet.entity.Admin;
import com.five.employnet.service.AdminService;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@Slf4j
@RestController
@RequestMapping("/admin")
@CrossOrigin(origins = CorsConfig.origins, allowCredentials = "true")
public class AdminController {
    final private AdminService adminService;
    final private JwtUtil jwtUtil;

    public AdminController(AdminService adminService, JwtUtil jwtUtil) {
        this.adminService = adminService;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("login")
    public R<String> login(@RequestBody Admin admin) {
        String password = DigestUtils.md5DigestAsHex(admin.getPassword().getBytes());
        String username = admin.getUsername();
        LambdaQueryWrapper<Admin> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Admin::getUsername, username);
        Admin a = adminService.getOne(queryWrapper);
        if (a == null) {
            return R.error("用户不存在");
        } else if (!Objects.equals(a.getPassword(), password)) {
            return R.error("密码错误");
        } else {

            String adminId = admin.getAdmin_id();
            String token = jwtUtil.generateToken(String.valueOf(adminId));
            return R.success(token);
        }
    }

    @GetMapping("test")
    public String test() {
        return "hello";
    }

    @PostMapping("register")
    public R<String> register(@RequestBody Admin admin) {
        String password = DigestUtils.md5DigestAsHex(admin.getPassword().getBytes());
        admin.setPassword(password);
        String username = admin.getUsername();
        LambdaQueryWrapper<Admin> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Admin::getUsername, username);
        Admin a = adminService.getOne(queryWrapper);
        if (a != null) {
            return R.error("用户已存在");
        }
        else {
            adminService.save(admin);
            return R.success("注册成功");
        }
    }

    @GetMapping
    public R<AdminDto> getOne() {
        String adminId = BaseContext.getCurrentId();
        Admin admin = adminService.getById(adminId);
        if (admin != null) {
            AdminDto adminDto = new AdminDto();
            BeanUtils.copyProperties(admin, adminDto);
            return R.success(adminDto);
        }
        return R.error("管理员不存在");
    }

    @PostMapping
    public R<AdminDto> updateOne(@RequestBody Admin admin) {
        String adminId = BaseContext.getCurrentId();
        if (admin.getAdmin_id() == null) admin.setAdmin_id(adminId);
        adminService.updateById(admin);
        AdminDto adminDto = new AdminDto();
        BeanUtils.copyProperties(admin, adminDto);
        return R.success(adminDto);
    }
}
